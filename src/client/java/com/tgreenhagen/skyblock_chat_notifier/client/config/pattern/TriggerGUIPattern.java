package com.tgreenhagen.skyblock_chat_notifier.client.config.pattern;

import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class TriggerGUIPattern {
    public TextFieldWidget triggerField;
    public TextFieldWidget messageField;
    public CheckboxWidget enabled;

    public TriggerGUIPattern(TextFieldWidget triggerField, TextFieldWidget messageField, CheckboxWidget enabled) {
        this.triggerField = triggerField;
        this.messageField = messageField;
        this.enabled = enabled;
    }
}
