// hooks/useMultistepForm.ts
import { useState, useCallback, useMemo } from 'react';

// Custom hook to manage multi-step form navigation
export function useMultistepForm(totalSteps: number, hasIntroScreen = false) {
    // Initialize currentStepIndex based on whether there's an intro screen
    const [currentStepIndex, setCurrentStepIndex] = useState(hasIntroScreen ? -1 : 0);

    /**
     * Memoized function to go to the next step.
     * Ensures that the function reference remains stable across renders.
     */
    const nextStep = useCallback(() => {
        setCurrentStepIndex((prevIndex) => {
            const newIndex = prevIndex + 1;
            return newIndex < totalSteps ? newIndex : prevIndex;
        });
    }, [totalSteps]);

    /**
     * Memoized function to go to the previous step.
     * Ensures that the function reference remains stable across renders.
     */
    const previousStep = useCallback(() => {
        setCurrentStepIndex((prevIndex) => {
            const newIndex = prevIndex - 1;
            // If there's an intro screen, don't allow going back beyond -1
            const minIndex = hasIntroScreen ? -1 : 0;
            return newIndex >= minIndex ? newIndex : prevIndex;
        });
    }, [hasIntroScreen]);

    /**
     * Memoized function to go to a specific step.
     * Ensures that the function reference remains stable across renders.
     */
    const goTo = useCallback((index: number) => {
        if (index >= (hasIntroScreen ? -1 : 0) && index < totalSteps) {
            setCurrentStepIndex(index);
        }
    }, [hasIntroScreen, totalSteps]);

    /**
     * Memoized function to start the form from the intro screen (if applicable).
     * Ensures that the function reference remains stable across renders.
     */
    const startForm = useCallback(() => {
        // If there's an intro screen, start at step 0; otherwise, ensure it's already at step 0
        setCurrentStepIndex(hasIntroScreen ? 0 : 0);
    }, [hasIntroScreen]);

    /**
     * Derived state to determine if the current step is the first step.
     * If there's an intro screen, the first actionable step is 0; otherwise, it's also 0.
     */
    const isFirstStep = useMemo(() => {
        // If there's an intro screen, the first step after intro is 0
        // If no intro screen, the first step is 0
        return currentStepIndex === 0;
    }, [currentStepIndex]);

    /**
     * Derived state to determine if the current step is the last step.
     */
    const isLastStep = useMemo(() => {
        return currentStepIndex === totalSteps - 1;
    }, [currentStepIndex, totalSteps]);

    /**
     * Derived state to determine if the current view is the intro screen.
     * Applicable only if hasIntroScreen is true.
     */
    const isIntroScreen = useMemo(() => {
        return hasIntroScreen && currentStepIndex === -1;
    }, [hasIntroScreen, currentStepIndex]);

    return {
        currentStepIndex,
        totalSteps,
        isFirstStep,
        isLastStep,
        nextStep,
        previousStep,
        goTo,
        startForm,
        hasIntroScreen,
        isIntroScreen,
    };
}
