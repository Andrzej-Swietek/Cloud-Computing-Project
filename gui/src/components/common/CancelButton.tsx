import type {ButtonHTMLAttributes} from 'react';
import {FC, ReactNode} from 'react';
import {Button} from "@components/ui/button.tsx";

interface CancelRetroButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  children?: ReactNode;
  variant?: 'primary' | 'secondary';
  className?: string;
  iconSize?: number | string;
  size?: 'sm' | 'md' | 'lg';
  onClick?: () => void;
}

export const CancelButton: FC<CancelRetroButtonProps> = ({
  iconSize = '4',
  size = 'md',
  onClick,
  className = '',
  children,
  variant = 'primary',
  ...props
}) => (
  <Button
    type="button"
    onClick={onClick}
    {...props}
  >
    Cancel
    {children}
  </Button>
);
