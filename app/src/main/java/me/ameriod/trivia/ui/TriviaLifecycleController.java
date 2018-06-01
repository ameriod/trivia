package me.ameriod.trivia.ui;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.archlifecycle.ControllerLifecycleRegistryOwner;

/**
 * Copy of {@link com.bluelinelabs.conductor.archlifecycle.LifecycleController}
 * but using a {@link LifecycleOwner} also added constructors to play nice with Kotlin
 */
public abstract class TriviaLifecycleController extends Controller implements LifecycleOwner {

    private final ControllerLifecycleRegistryOwner lifecycleRegistryOwner = new ControllerLifecycleRegistryOwner(this);

    public TriviaLifecycleController() {
        super();
    }

    public TriviaLifecycleController(@Nullable Bundle args) {
        super(args);
    }

    @NonNull
    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistryOwner.getLifecycle();
    }

}

