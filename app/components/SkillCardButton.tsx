import React from 'react';

interface SkillCardButtonProps {
    onClick: () => void;
    label?: string;
    className?: string;
}

export const SkillCardButton: React.FC<SkillCardButtonProps> = ({
    onClick,
    label = 'Add Skill',
    className = '',
}) => {
    return (
        <button
            onClick={onClick}
            className={`px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors duration-200 flex items-center gap-2 ${className}`}
            type="button"
        >
            <span className="text-xl">➕</span>
            <span>{label}</span>
        </button>
    );
};

export default SkillCardButton;