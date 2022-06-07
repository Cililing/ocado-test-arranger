/*
 * Copyright © 2020 Ocado (marian.jureczko@ocado.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ocadotechnology.gembus.test.easyrandom;

import org.jeasy.random.ObjectCreationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.function.Function;

public abstract class RecordReflectionUtils {

    public static <T> T generateRecord(Class<T> type, Function<RecordComponent, Object> recordParamsCreator) {
        Object[] constructorParams = arrangeRecordConstructorParams(type, recordParamsCreator);
        return instantiateRecord(type, constructorParams);
    }

    public static <T> T instantiateRecord(Class<T> recordType, Object[] constructorParameters) {
        try {
            Class<?>[] componentTypes = Arrays.stream(recordType.getRecordComponents())
                    .map(RecordComponent::getType)
                    .toArray(Class[]::new);
            Constructor<T> constructor = recordType.getDeclaredConstructor(componentTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(constructorParameters);
        } catch (Exception e) {
            throw new ObjectCreationException("Unable to create a random instance of recordType " + recordType + ". You may need to cover it with a Custom Arranger.", e);
        }
    }

    public static <T> Object[] arrangeRecordConstructorParams(Class<T> recordType, Function<RecordComponent, Object> arrangeRandom) {
        return Arrays.stream(recordType.getRecordComponents())
                .map(arrangeRandom)
                .toArray(Object[]::new);
    }
}
