package com.github.neunkasulle.chronocommand.view;

import com.vaadin.data.Item;
import com.vaadin.data.util.PropertyValueGenerator;

import java.time.LocalDateTime;

/**
 * Created by Ming-Samsung on 2016/1/31.
 */
public class LocalDateTimeToLocalTimeStringConverter extends  PropertyValueGenerator<String> {

    private final Object propertyId;

    public LocalDateTimeToLocalTimeStringConverter(final Object propertyId) {
        this.propertyId = propertyId;
    }

    @Override
    public String getValue(final Item item, final Object itemId,
                           final Object propertyId) {
        final LocalDateTime time = (LocalDateTime)
                item.getItemProperty(this.propertyId).getValue();
        if (time == null) {
            return "-";
        }
        return time.toLocalTime().toString();
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}
