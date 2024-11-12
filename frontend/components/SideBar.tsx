import {RoughNotation} from "react-rough-notation";

// Define the type for props passed into the `SideBar` component.
type NavProps = {
    currentStepIndex: number;
    goTo: (index: number) => void;
};


const steps = [
    {label: 'Datasource', textColorClass: 'text-yellow-400', underlineColor: '#ffe666'},
    {label: 'Select Mode', textColorClass: 'text-red-600', underlineColor: '#bd284d'},
    {label: 'Select Split', textColorClass: 'text-purple-300', underlineColor: '#E7B8FF'},
    {label: 'Select Structure', textColorClass: 'text-green-400', underlineColor: '#6fe79f'},
    {label: 'Select Schema Noise', textColorClass: 'text-orange-500', underlineColor: '#FF6347'},
    {label: 'Select Data Noise', textColorClass: 'text-orange-500', underlineColor: '#FF6347'},
    {label: 'Select Shuffle', textColorClass: 'text-teal-400', underlineColor: '#20B2AA'},
    {label: 'Summary', textColorClass: 'text-green-400', underlineColor: '#6fe79f'},
];

const SideBar = ({currentStepIndex, goTo}: NavProps) => {
    return (
        <div className="absolute -top-20 left-0 w-full md:w-[25%] md:relative md:top-0 md:left-0">
            <nav className="py-5 text-slate-200 bg-neutral-900 h-full rounded-md border border-neutral-700 md:p-5">
                <ul className="flex justify-center gap-2 md:flex-col">
                    {steps.map((step, index) => (
                        <li key={index} className="flex flex-col items-start font-medium">
              <span className="hidden text-neutral-500 uppercase text-sm md:flex">
                step {index + 1}
              </span>
                            <button
                                tabIndex={0}
                                onClick={() => goTo(index)}
                                className={`text-sm md:text-base ${
                                    currentStepIndex === index ? step.textColorClass : 'text-white'
                                }`}
                            >
                                <RoughNotation
                                    type="underline"
                                    show={currentStepIndex === index}
                                    color={step.underlineColor}
                                >
                                    {step.label}
                                </RoughNotation>
                            </button>
                        </li>
                    ))}
                </ul>
            </nav>
        </div>
    );
};

export default SideBar;
