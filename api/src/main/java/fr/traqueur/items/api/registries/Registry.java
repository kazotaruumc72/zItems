package fr.traqueur.items.api.registries;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

import java.util.Collection;

public interface Registry<ID, T> {

    ClassToInstanceMap<Registry<?,?>> INSTANCES = MutableClassToInstanceMap.create();

    static <T extends Registry<?,?>> T get(Class<T> clazz) {
        return INSTANCES.getInstance(clazz);
    }

    static <T extends Registry<?,?>> void register(Class<T> clazz, T instance) {
        INSTANCES.putInstance(clazz, instance);
    }


    void register(ID id, T item);

    T getById(ID id);

    Collection<T> getAll();

}
