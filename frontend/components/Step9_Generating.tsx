"use client";

import { motion } from "framer-motion";
import { Loader2 } from "lucide-react";

const generatingVariants = {
    hidden: {
        opacity: 0,
        y: 50,
    },
    visible: {
        opacity: 1,
        y: 0,
        transition: {
            ease: "easeInOut",
            duration: 0.5,
        },
    },
};

const loaderVariants = {
    animate: {
        rotate: 360,
        transition: {
            repeat: Infinity,
            duration: 1.2,
            ease: "linear",
        },
    },
};

const Step9_Generating = () => {
    return (
        <motion.section
            className="w-full h-full flex flex-col items-center justify-center gap-4 text-center"
            variants={generatingVariants}
            initial="hidden"
            animate="visible"
        >
            {/* Animated loader icon */}
            <motion.div variants={loaderVariants} animate="animate">
                <Loader2 className="h-16 w-16 text-white" />
            </motion.div>

            {/* Loading message */}
            <h4 className="text-2xl font-semibold text-white md:text-3xl mt-4">
                Generating datasets...
            </h4>
            <p className="text-sm max-w-md text-neutral-300 md:text-base">
                Please wait while we create your datasets.
            </p>
        </motion.section>
    );
};

export default Step9_Generating;
