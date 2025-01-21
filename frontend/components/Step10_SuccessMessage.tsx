"use client";

import Image from "next/image";
import { motion } from "framer-motion";
import { RefreshCcw, Download } from "lucide-react";
import { Button } from "@/components/ui/button";
import successIcon from "../public/assets/success.png";
import React from "react";

interface Step10_SuccessMessageProps {
    downloadUrl: string;
}

const successVariants = {
    hidden: {
        opacity: 0,
        y: 50,
    },
    visible: {
        opacity: 1,
        y: 0,
        transition: {
            ease: "backIn",
            duration: 0.6,
        },
    },
};


const Step10_SuccessMessage: React.FC<Step10_SuccessMessageProps> = ({ downloadUrl }) => {

    const refresh = () => window.location.reload();

    //Handles the download of the ZIP file.
    const handleDownload = () => {
        if (!downloadUrl) {
            alert("Download link is not available.");
            return;
        }

        const link = document.createElement('a');
        link.href = downloadUrl;
        link.download = 'datasets.zip';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);

        // Revoke the object URL after a short delay to ensure the download has started
        setTimeout(() => {
            window.URL.revokeObjectURL(downloadUrl);
        }, 1000);
    };

    return (
        <motion.div
            className="flex items-center justify-center w-full h-full"
            variants={successVariants}
            initial="hidden"
            animate="visible"
        >
            <div className="flex flex-col items-center gap-6 md:gap-4 text-center">
                <Image
                    src={successIcon}
                    width={150}
                    height={150}
                    alt="Success Icon"
                    className="md:mb-4"
                />

                <h4 className="text-2xl font-semibold text-white md:text-3xl">
                    Datasets Generated Successfully!
                </h4>

                <p className="text-sm max-w-md text-neutral-300 md:text-base">
                    Your datasets have been successfully generated. You can now download the ZIP file containing your data.
                </p>

                <div className="flex items-center mt-6 gap-4">
                    {/* Download Button */}
                    <Button
                        onClick={handleDownload}
                        className="flex items-center text-neutral-200 bg-green-600 border border-green-700 shadow-input shadow-black/10 rounded-xl hover:bg-green-700"
                    >
                        <Download className="mr-2 h-4 w-4" />
                        Download ZIP
                    </Button>

                    {/* Restart Button */}
                    <Button
                        onClick={refresh}
                        className="flex items-center text-neutral-200 bg-neutral-900 border border-black/20 shadow-input shadow-black/10 rounded-xl hover:text-white"
                    >
                        <RefreshCcw className="mr-2 h-4 w-4" />
                        Restart
                    </Button>
                </div>
            </div>
        </motion.div>
    );
};

export default Step10_SuccessMessage;
