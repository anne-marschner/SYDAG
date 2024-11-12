import { useState } from 'react';

// Custom hook to manage multi-step form navigation
export function useMultistepForm(totalSteps: number, hasIntroScreen = false) {
    const [currentStepIndex, setCurrentStepIndex] = useState(hasIntroScreen ? -1 : 0);

    // Helper booleans
    const isFirstStep = currentStepIndex === (hasIntroScreen ? 0 : 0);
    const isLastStep = currentStepIndex === totalSteps - 1;

    // Function to go to the next step
    const nextStep = () => {
        setCurrentStepIndex((prevIndex) => {
            const newIndex = prevIndex + 1;
            return newIndex < totalSteps ? newIndex : prevIndex;
        });
    };

    // Function to go to the previous step
    const previousStep = () => {
        setCurrentStepIndex((prevIndex) => {
            const newIndex = prevIndex - 1;
            return newIndex >= 0 ? newIndex : prevIndex;
        });
    };

    // Function to go to a specific step
    const goTo = (index: number) => {
        if (index >= -1 && index < totalSteps) {
            setCurrentStepIndex(index);
        }
    };

    // Function to start the form from the intro screen (if applicable)
    const startForm = () => setCurrentStepIndex(0);

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
        isIntroScreen: currentStepIndex === -1,
    };
}
