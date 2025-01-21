import React from "react";
import * as ToggleComp from "@radix-ui/react-toggle";
import { cn } from "@/lib/utils";

const Toggle = React.forwardRef<
    React.ElementRef<typeof ToggleComp.Root>,
    React.ComponentPropsWithoutRef<typeof ToggleComp.Root>
>(({ className, ...props }, ref) => (
    <ToggleComp.Root
        ref={ref}
        aria-label="Toggle headers"
        className={cn(
            "h-5 w-10 shrink-0 rounded-full border-2 border-slate-300 focus:outline-none focus:ring-2 focus:ring-slate-400 focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 dark:border-slate-700 dark:bg-slate-800 data-[state=on]:bg-green-500 data-[state=on]:border-green-500 transition-colors",
            className
        )}
        {...props}
    />
));

Toggle.displayName = ToggleComp.Root.displayName;

export { Toggle };
