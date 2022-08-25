package ui;

import javax.swing.*;
import java.awt.*;

class ComboBoxRenderer extends JLabel implements ListCellRenderer
{
    private String _title;

    public ComboBoxRenderer(String title)
    {
        _title = title;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus)
    {
        if (index == -1 && value == null) setText(_title);
        else setText(value.toString());
        return this;
    }
}