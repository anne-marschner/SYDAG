"use client";
import React from "react";
import { Button } from "@/components/ui/button";

const Step0_Start = ({ onStart }: { onStart: () => void }) => {
    return (
        <div className="flex flex-col justify-center items-center h-screen text-center">
            <h1 className="text-3xl font-bold text-white mb-4">Welcome to SYDAG</h1>
            <p className="text-neutral-300 mb-8 max-w-lg">
                This application generates synthetic datasets for data integration. <br /> It includes the following steps:
            </p>
            <ul className="text-neutral-300 text-left mb-8 max-w-lg list-disc list-inside space-y-2">
                <li><strong>Datasource</strong>: Upload a CSV file as the data source.</li>
                <li><strong>Select Mode</strong>: Use a JSON template or manual input.</li>
                <li><strong>Select Split</strong>: Split the dataset horizontally and/or vertically.</li>
                <li><strong>Select Structure</strong>: Choose structural changes.</li>
                <li><strong>Schema Noise</strong>: Add noise to the schema.</li>
                <li><strong>Data Noise</strong>: Add noise to the data values.</li>
                <li><strong>Shuffle</strong>: Randomize the data order.</li>
            </ul>
            <Button
                onClick={onStart}
                className="text-neutral-200 bg-neutral-900 border border-black/20 shadow-input shadow-black/10 rounded-xl hover:text-white text-lg py-4 px-8"
            >
                Start
            </Button>
        </div>
    );
};

export default Step0_Start;
