import {FC} from 'react';
import {CancelButton} from '@components/common/CancelButton.tsx';
import {Dialog} from "@components/ui/dialog.tsx";
import {Button} from "@components/ui/button.tsx";

export const ConfirmModal: FC<{
  open: boolean;
  onClose: () => void;
  onConfirm: () => void;
  children?: React.ReactNode;
}> = ({ open, onClose, onConfirm, children }) => (
  <>
    {open && (
      <Dialog open={open}>
        <div className="flex flex-col gap-6 items-center p-6">
          <div
            className="font-black text-2xl leading-[100%] tracking-[0%] align-bottom text-[var(--primary-black)]"
          >
            {children}
          </div>
          <div className="flex gap-4">
            <CancelButton
              onClick={onClose}
            />
            <Button
              onClick={onConfirm}
            >
              Delete
            </Button>
          </div>
        </div>
      </Dialog>
    )}
  </>
);
