"use client";
import Image from "next/image";
import {motion} from "framer-motion";
import {RefreshCcw} from "lucide-react";
import {Button} from "@/components/ui/button";
import successIcon from "../public/assets/success.png";

// Defining animation variants for the success message, used by Framer Motion.
// These control how the element appears or moves when entering or exiting.
const successVariants = {
    hidden: {
        opacity: 0,  // Initially hidden with opacity set to 0.
        y: 50,       // Positioned 50 pixels down (out of view).
    },
    visible: {
        opacity: 1,  // Visible with full opacity.
        y: 0,        // Brought back to its original position.
        transition: {
            ease: "backIn",  // The easing function for the animation.
            duration: 0.6,   // Animation duration of 0.6 seconds.
        },
    },
};

// Component for rendering the success message.
const Step9_SuccessMessage = () => {

    // Function to reload the page when the user clicks the "Restart" button.
    const refresh = () => window.location.reload();

    return (
        // `motion.section` is a Framer Motion component that animates the section element.
        // The `variants` prop takes the animation variants defined above (successVariants).
        // `initial` specifies the initial animation state (hidden), and `animate` specifies the final state (visible).
        <motion.section
            className="w-full h-full flex flex-col items-center justify-center gap-4 md:gap-2 text-center"
            variants={successVariants}
            initial="hidden"
            animate="visible"
        >

            {/* Next.js optimized image rendering for the success icon */}
            <Image
                src={successIcon}        // Path to the success icon image.
                width="150"              // Image width of 150 pixels.
                height="150"             // Image height of 150 pixels.
                alt="Success Icon"       // Accessible alt text for the image.
                className="md:mb-4"      // Margin-bottom for medium screens.
            />

            {/* A heading to thank the user */}
            <h4 className="text-2xl font-semibold text-white md:text-3xl">
                Response to be implemented
            </h4>

            {/* Paragraph for the success message, with some styling for responsiveness */}
            <p className="text-sm max-w-md text-neutral-300 md:text-base">
                
            </p>

            {/* A button that reloads the page when clicked */}
            <div className="flex items-center mt-6">
                {/* Styling wrapper for the button, with a custom shadow effect */}
                <div
                    className="relative after:pointer-events-none after:absolute after:inset-px after:rounded-[11px] after:shadow-highlight after:shadow-white/10 focus-within:after:shadow-[#77f6aa] after:transition">

                    {/* Button component from the UI folder. It triggers the refresh function on click. */}
                    <Button
                        onClick={refresh}  // Calls the `refresh` function to reload the page.
                        className="relative text-neutral-200 bg-neutral-900 border border-black/20 shadow-input shadow-black/10 rounded-xl hover:text-white"
                    >
                        {/* An icon (refresh icon) inside the button */}
                        <RefreshCcw className="mr-2 h-4 w-4"/>
                        Restart
                    </Button>
                </div>
            </div>
        </motion.section>
    );
};

export default Step9_SuccessMessage; // Exporting the component as default.
