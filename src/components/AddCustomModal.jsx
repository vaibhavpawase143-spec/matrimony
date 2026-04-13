import { useState } from 'react';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';

const AddCustomModal = ({ isOpen, onClose, onAdd, fieldType }) => {
  const [customValue, setCustomValue] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (customValue.trim()) {
      onAdd(customValue.trim());
      setCustomValue('');
    }
  };

  const handleClose = () => {
    setCustomValue('');
    onClose();
  };

  const getFieldLabel = () => {
    const labels = {
      religion: 'Religion',
      caste: 'Caste',
      education: 'Education',
      occupation: 'Occupation',
      maritalStatus: 'Marital Status',
      location: 'Location',
      state: 'State',
      city: 'City',
    };
    return labels[fieldType] || fieldType;
  };

  return (
    <Dialog open={isOpen} onOpenChange={handleClose}>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Add Custom {getFieldLabel()}</DialogTitle>
          <DialogDescription>
            Enter a new {getFieldLabel().toLowerCase()} option that will be added to the list.
          </DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit}>
          <div className="grid gap-4 py-4">
            <div className="grid grid-cols-4 items-center gap-4">
              <Label htmlFor="custom-value" className="text-right">
                {getFieldLabel()}
              </Label>
              <Input
                id="custom-value"
                value={customValue}
                onChange={(e) => setCustomValue(e.target.value)}
                className="col-span-3"
                placeholder={`Enter ${getFieldLabel().toLowerCase()}`}
                autoFocus
              />
            </div>
          </div>
          <DialogFooter>
            <Button type="button" variant="outline" onClick={handleClose}>
              Cancel
            </Button>
            <Button type="submit" disabled={!customValue.trim()}>
              Add
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default AddCustomModal;