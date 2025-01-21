import { useState, useCallback, useMemo } from 'react';

export function useMultistepForm(totalSteps: number, hasIntroScreen = false) {
    // Initialize currentStepIndex based on whether there's an intro screen
    const [currentStepIndex, setCurrentStepIndex] = useState(hasIntroScreen ? -1 : 0);

    /**
     * Function to go to the next step.
     */
    const nextStep = useCallback(() => {
        setCurrentStepIndex((prevIndex) => {
            const newIndex = prevIndex + 1;
            return newIndex < totalSteps ? newIndex : prevIndex;
        });
    }, [totalSteps]);

    /**
     * Function to go to the previous step.
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
     * Function to go to a specific step.
     */
    const goTo = useCallback((index: number) => {
        if (index >= (hasIntroScreen ? -1 : 0) && index < totalSteps) {
            setCurrentStepIndex(index);
        }
    }, [hasIntroScreen, totalSteps]);

    /**
     * Function to start the form from the intro screen (if applicable).
     */
    const startForm = useCallback(() => {
        // If there's an intro screen, start at step 0; otherwise, ensure it's already at step 0
        setCurrentStepIndex(hasIntroScreen ? 0 : 0);
    }, [hasIntroScreen]);

    /**
     * State to determine if the current step is the first step.
     */
    const isFirstStep = useMemo(() => {
        return currentStepIndex === 0;
    }, [currentStepIndex]);

    /**
     * State to determine if the current step is the last step.
     */
    const isLastStep = useMemo(() => {
        return currentStepIndex === totalSteps - 1;
    }, [currentStepIndex, totalSteps]);

    /**
     * State to determine if the current view is the intro screen.
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
