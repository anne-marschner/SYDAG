import {ReactNode} from "react";
import {motion} from "framer-motion"; 

// Define the types for the props that will be passed into the `FormWrapper` component
type FormWrapperProps = {
    title: string;
    description: string; 
    children: ReactNode; 
};

// Define animation variants for the form container using Framer Motion
const formVariants = {
    hidden: {
        opacity: 0,
        x: -50,     
    },
    visible: {
        opacity: 1,  
        x: 0,       
    },
    exit: {
        opacity: 0,
        x: 50,
        transition: {
            ease: "easeOut",
        },
    },
};

// `FormWrapper` component is responsible for rendering a form
const FormWrapper = ({title, description, children}: FormWrapperProps) => {
    return (
        <motion.div
            className="flex flex-col gap-5"
            variants={formVariants}
            initial="hidden" 
            animate="visible"
            exit="exit"
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

export default FormWrapper;
