package com.tyron.builder.compiler.manifest;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;


import javax.annotation.concurrent.Immutable;

/**
 * Represents a selector to be able to identify manifest file xml elements.
 */
@Immutable
public class Selector {

    /**
     * local name for tools:selector attributes.
     */
    public static final String SELECTOR_LOCAL_NAME = "selector";

    @NonNull private final String mPackageName;

    public Selector(@NonNull String packageName) {
        mPackageName = Preconditions.checkNotNull(packageName);
    }

    /**
     * Returns true if the passed element is "selected" by this selector. If so, any action this
     * selector decorated will be applied to the element.
     */
    boolean appliesTo(XmlElement element) {
        Optional<XmlAttribute> packageName = element.getDocument().getPackage();
        return packageName.isPresent() && mPackageName.equals(packageName.get().getValue());
    }

    /**
     * Returns true if the passed resolver can resolve this selector, false otherwise.
     */
    boolean isResolvable(KeyResolver<String> resolver) {
        return resolver.resolve(mPackageName) != null;
    }

    @Override
    public String toString() {
        return mPackageName;
    }
}
