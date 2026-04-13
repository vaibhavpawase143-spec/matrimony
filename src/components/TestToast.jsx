import { useToast } from './Toast';

const TestToast = () => {
  const { success, error, warning, info } = useToast();

  const testToasts = () => {
    success('Success test!');
    error('Error test!');
    warning('Warning test!');
    info('Info test!');
  };

  return (
    <button 
      onClick={testToasts}
      className="fixed bottom-4 right-4 bg-blue-500 text-white px-4 py-2 rounded-lg z-50"
    >
      Test Toasts
    </button>
  );
};

export default TestToast;
