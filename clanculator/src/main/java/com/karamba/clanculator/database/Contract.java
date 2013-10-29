package com.karamba.clanculator.database;

import com.karamba.clanculator.utils.Utils;

/**
 * todo: javadocs.
 */
public interface Contract {
    public interface Settings {
        public static final String TABLE_NAME = Utils.camelCaseToUnderscore(Settings.class.getSimpleName());

        public static final String NAME = "name";
        public static final String VALUE = "value";
    }

    public interface Inventory {
        public static final String TABLE_NAME = Utils.camelCaseToUnderscore(Inventory.class.getSimpleName());

        public static final String _ID = "_id";
        public static final String TYPE = "type";
        public static final String NUMBER = "number";
        public static final String LEVEL = "level";
    }
}
