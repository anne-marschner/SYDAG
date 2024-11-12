import {Ubuntu} from "next/font/google";
import "../styles/globals.css";
import React from "react";

const ubuntu = Ubuntu({
    variable: "--font-ubuntu",
    weight: ["400", "500", "700"],
    subsets: ["latin"],
    display: "swap",
});

export default function RootLayout({children}: { children: React.ReactNode }) {
    return (
        <html lang="en" className={`${ubuntu.variable} text-neutral-200`}>
        <head><title></title></head>
        <body className="flex justify-center items-center h-screen w-full bg-[#0d0f12]">
        {children}
        </body>
        </html>
    );
}
