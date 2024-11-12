// "@/components/ui/slider.tsx"
import React from 'react';
import * as SliderComp from "@radix-ui/react-slider";

export interface SliderProps extends React.ComponentPropsWithoutRef<typeof SliderComp.Root> {
    className?: string;
}

const Slider = React.forwardRef<
    React.ElementRef<typeof SliderComp.Root>,
    SliderProps
>(({ className, ...props }, ref) => (
    <form className="w-full">
        <SliderComp.Root
            ref={ref}
            className={`relative flex items-center select-none touch-none w-full h-5 ${className}`}
            {...props}
        >
            {/* Track */}
            <SliderComp.Track className="bg-neutral-600 relative grow rounded-full h-[4px]">
                <SliderComp.Range className="absolute bg-[#77f6aa] rounded-full h-full" />
            </SliderComp.Track>

            {/* Thumb */}
            <SliderComp.Thumb
                className="block w-5 h-5 bg-white border border-neutral-600 shadow-lg rounded-full hover:bg-[#77f6aa] focus:outline-none focus:ring-2 focus:ring-[#77f6aa]"
                aria-label="Slider Thumb"
            />
        </SliderComp.Root>
    </form>
));

Slider.displayName = 'Slider';

export { Slider };
