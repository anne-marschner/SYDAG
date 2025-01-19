import {ReactNode} from "react"; // Importing ReactNode type to type the `children` prop (used for rendering nested components).
import {motion} from "framer-motion"; // Importing the `motion` component from Framer Motion for animation handling.

// Defining the types for the props that will be passed into the `FormWrapper` component.
type FormWrapperProps = {
    title: string; // The title of the form section.
    description: string; // A brief description of the form section.
    children: ReactNode; // The content (or form fields) passed as children, rendered inside the wrapper.
};

// Defining animation variants for the form container using Framer Motion.
// These variants control how the form appears on the screen (with opacity and position changes).
const formVariants = {
    hidden: {
        opacity: 0,  // Initially hidden (opacity set to 0).
        x: -50,      // Initially positioned 50 pixels to the left (off-screen).
    },
    visible: {
        opacity: 1,  // Fully visible (opacity set to 1).
        x: 0,        // Moves to its final position (x: 0, i.e., centered).
    },
    exit: {
        opacity: 0,  // Fades out (opacity set to 0).
        x: 50,       // Moves 50 pixels to the right during exit.
        transition: {
            ease: "easeOut", // Smooth easing for the exit transition.
        },
    },
};

// The `FormWrapper` component is responsible for rendering a form section with a title, description, and form content.
const FormWrapper = ({title, description, children}: FormWrapperProps) => {
    return (
        // `motion.div` from Framer Motion is used to animate the container div.
        // The `variants` prop defines the animation behavior (defined in `formVariants`).
        // The `initial` state is "hidden", and it transitions to "visible" when mounted, and exits with the "exit" variant.
        <motion.div
            className="flex flex-col gap-5" // Using Tailwind CSS classes for layout and spacing.
            variants={formVariants} // Applying the animation variants defined earlier.
            initial="hidden" // The form starts in the hidden state.
            animate="visible" // When the component mounts, it animates to the visible state.
            exit="exit" // When the component is removed, it animates using the exit variant.
        >
            {/* The header section of the form, containing the title and description. */}
            <div className="flex flex-col gap-2">
                {/* The form title, styled with a larger font size and white color for contrast. */}
                <h2 className="text-xl font-semibold text-white md:text-2xl">
                    {title} {/* Displays the title passed as a prop. */}
                </h2>

                {/* A brief description of the form section, styled with smaller font size and neutral color. */}
                <p className="text-sm text-neutral-300 md:text-base">
                    {description} {/* Displays the description passed as a prop. */}
                </p>

            </div>
            {/* Rendering the children (form content or components) passed into the FormWrapper. */}
            {children}

        </motion.div>
    );
};

export default FormWrapper; // Exporting the `FormWrapper` component for use in other parts of the application.
