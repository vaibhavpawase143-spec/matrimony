import { useState } from 'react';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Button } from '@/components/ui/button';
import { Plus } from 'lucide-react';
import AddCustomModal from './AddCustomModal';

const MatrimonySelect = ({
  value,
  onChange,
  options,
  placeholder,
  fieldType,
  onAddCustom,
  ...props
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleAddCustom = (newValue) => {
    onAddCustom(fieldType, newValue);
    onChange(newValue);
    setIsModalOpen(false);
  };

  return (
    <>
      <Select value={value} onValueChange={onChange} {...props}>
        <SelectTrigger>
          <SelectValue placeholder={placeholder} />
        </SelectTrigger>
        <SelectContent>
          {options.map((option) => (
            <SelectItem key={option} value={option}>
              {option}
            </SelectItem>
          ))}
          <div className="px-2 py-1.5">
            <Button
              variant="ghost"
              size="sm"
              className="w-full justify-start text-primary hover:text-primary"
              onClick={() => setIsModalOpen(true)}
            >
              <Plus className="h-4 w-4 mr-2" />
              Add Custom
            </Button>
          </div>
        </SelectContent>
      </Select>

      <AddCustomModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onAdd={handleAddCustom}
        fieldType={fieldType}
      />
    </>
  );
};

export default MatrimonySelect;