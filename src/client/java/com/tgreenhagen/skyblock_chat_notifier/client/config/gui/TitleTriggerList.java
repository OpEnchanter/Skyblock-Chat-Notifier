package com.tgreenhagen.skyblock_chat_notifier.client.config.gui;

import com.tgreenhagen.skyblock_chat_notifier.client.config.pattern.TriggerGUIPattern;
import com.tgreenhagen.skyblock_chat_notifier.client.config.pattern.TriggerPattern;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class TitleTriggerList extends AbstractConfigListEntry<List<TriggerPattern>> {

    private final MinecraftClient client;

    // Working copy of the list we edit in the UI
    private final List<TriggerPattern> value;
    private final List<TriggerPattern> defaultValue;

    // Called with the final list when the screen saves
    private final Consumer<List<TriggerPattern>> saveConsumer;

    // One GUI row per TriggerPattern
    private final List<TriggerGUIPattern> rows = new ArrayList<>();

    private ButtonWidget addNewButton;
    private ButtonWidget removeLastButton;

    public TitleTriggerList(Text fieldName,
                            List<TriggerPattern> value,
                            List<TriggerPattern> defaultValue,
                            Consumer<List<TriggerPattern>> saveConsumer) {
        super(fieldName, false);
        this.client = MinecraftClient.getInstance();

        this.value = new ArrayList<>();
        this.defaultValue = new ArrayList<>();

        if (value == null ) {
            this.value.add(new TriggerPattern("new trigger", "new message", true));
            this.defaultValue.add(new TriggerPattern("new trigger", "new message", true));
        } else {
            // Make a mutable working copy of the current config value
            if (value != null) {
                for (TriggerPattern pattern : value) {
                    this.value.add(pattern); // copy ctor
                }
            }

            // Copy defaults as well (can be empty)
            if (defaultValue != null) {
                for (TriggerPattern pattern : defaultValue) {
                    this.defaultValue.add(new TriggerPattern(pattern.trigger, pattern.message, pattern.enabled));
                }
            }
        }

        this.saveConsumer = saveConsumer;
    }

    @Override
    public void render(DrawContext ctx,
                       int index,
                       int y,
                       int x,
                       int entryWidth,
                       int entryHeight,
                       int mouseX,
                       int mouseY,
                       boolean isSelected,
                       float delta) {

        // Draw label
        ctx.drawTextWithShadow(
                client.textRenderer,
                getFieldName(),
                x,
                y + 2,
                0xFFFFFF
        );

        // Layout for rows
        int rowY = y + 14;           // first row under label
        int rowHeight = 22;
        int widgetX = x + 10;
        int widgetWidth = (entryWidth - 10 - 10 - 40) / 2; // 10 left, 10 right padding, 40 reserved if you later add a remove button

        // Ensure we have the same number of GUI rows as data entries
        if (rows.isEmpty()) {
            for (int i = 0; i < value.size(); i++) {
                TriggerPattern pattern = value.get(i);

                TextFieldWidget triggerField = new TextFieldWidget(
                        client.textRenderer,
                        widgetX,
                        rowY,
                        widgetWidth,
                        rowHeight - 2,
                        Text.literal("Trigger")
                );
                triggerField.setText(pattern.trigger);

                TextFieldWidget messageField = new TextFieldWidget(
                        client.textRenderer,
                        widgetX + widgetWidth + 5,
                        rowY,
                        widgetWidth,
                        rowHeight - 2,
                        Text.literal("Message")
                );
                messageField.setText(pattern.message);

                CheckboxWidget checkbox = CheckboxWidget.builder(Text.literal(""), client.textRenderer)
                        .pos(widgetX + 2 * widgetWidth + 10, rowY)
                        .maxWidth(20)
                        .checked(pattern.enabled)
                        .build();

                rows.add(new TriggerGUIPattern(triggerField, messageField, checkbox));

                rowY += rowHeight;
            }

            // Create list modification buttons
            addNewButton = ButtonWidget.builder(
                            Text.literal("Add new"),
                            btn -> { // Callback
                                rows.clear();
                                value.add(new TriggerPattern("Trigger", "Message", true));
                            }
                    )
                    .position((entryWidth - 80) / 2, y + (value.size() * rowHeight) + 16)
                    .size(80, rowHeight - 2)
                    .build();

            removeLastButton = ButtonWidget.builder(
                            Text.literal("Remove last"),
                            btn -> { // Callback
                                if (rows.size() > 0) {
                                    rows.clear();
                                    value.remove(value.size() - 1);
                                }
                            }
                    )
                    .position((entryWidth - 80) / 2 + 82, y + (value.size() * rowHeight) + 16)
                    .size(80, rowHeight - 2)
                    .build();
        } else {
            for (TriggerGUIPattern pattern : rows) {
                pattern.triggerField.setPosition(widgetX, rowY);
                pattern.messageField.setPosition(widgetX + widgetWidth + 5, rowY);
                pattern.enabled.setPosition(widgetX + 2 * widgetWidth + 10, rowY);

                rowY += rowHeight;
            }
        }



        // Render all rows and buttons
        for (TriggerGUIPattern row : rows) {
            row.triggerField.render(ctx, mouseX, mouseY, delta);
            row.messageField.render(ctx, mouseX, mouseY, delta);
            row.enabled.render(ctx, mouseX, mouseY, delta);
        }

        addNewButton.render(ctx, mouseX, mouseY, delta);
        removeLastButton.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public int getItemHeight() {
        // Label + N rows + small padding
        int base = 18;
        int rowHeight = 22;
        return base + value.size() * rowHeight + 6;
    }

    // Cloth uses this in newer versions instead of Optional<T> getValue()
    @Override
    public List<TriggerPattern> getValue() {
        // Sync GUI back into value list
        for (int i = 0; i < value.size() && i < rows.size(); i++) {
            TriggerGUIPattern row = rows.get(i);
            TriggerPattern pattern = value.get(i);

            pattern.trigger = row.triggerField.getText();
            pattern.message = row.messageField.getText();
            pattern.enabled = row.enabled.isChecked();
        }
        return value;
    }

    @Override
    public Optional<List<TriggerPattern>> getDefaultValue() {
        return Optional.of(defaultValue);
    }

    @Override
    public boolean isEdited() {
        getValue();
        if (value.size() != defaultValue.size()) {
            return true;
        }
        for (int i = 0; i < value.size(); i++) {
            TriggerPattern v = value.get(i);
            TriggerPattern d = defaultValue.get(i);
            if (!v.trigger.equals(d.trigger)
                    || !v.message.equals(d.message)
                    || v.enabled != d.enabled) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void save() {
        // Ensure we read the latest text from widgets
        List<TriggerPattern> current = getValue();
        // Push to config via consumer
        saveConsumer.accept(new ArrayList<>(current));
    }

    @Override
    public List<? extends Element> children() {
        if (rows.isEmpty()) return List.of(addNewButton, removeLastButton);

        List<Element> elements = new ArrayList<>();
        for (TriggerGUIPattern row : rows) {
            elements.add(row.triggerField);
            elements.add(row.messageField);
            elements.add(row.enabled);
        }
        elements.add(addNewButton);
        elements.add(removeLastButton);
        return elements;
    }

    @Override
    public List<? extends Selectable> narratables() {
        // If you care about narration, you can add widgets that implement Selectable here.
        return Collections.emptyList();
    }
}