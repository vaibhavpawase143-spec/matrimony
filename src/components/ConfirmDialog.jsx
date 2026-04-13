import React from 'react';
import { AlertTriangle, X, Check } from 'lucide-react';
import { motion } from 'framer-motion';

const ConfirmDialog = ({ 
  isOpen, 
  onClose, 
  onConfirm, 
  title = "Confirm Action", 
  message = "Are you sure you want to proceed?",
  confirmText = "Confirm",
  cancelText = "Cancel",
  type = "danger" // danger, warning, info
}) => {
  if (!isOpen) return null;

  const getIconAndColors = () => {
    switch (type) {
      case 'danger':
        return {
          icon: <AlertTriangle className="h-6 w-6 text-red-500" />,
          bgClass: "bg-red-50 border-red-200",
          buttonClass: "bg-red-600 hover:bg-red-700 text-white"
        };
      case 'warning':
        return {
          icon: <AlertTriangle className="h-6 w-6 text-yellow-500" />,
          bgClass: "bg-yellow-50 border-yellow-200",
          buttonClass: "bg-yellow-600 hover:bg-yellow-700 text-white"
        };
      case 'info':
        return {
          icon: <Check className="h-6 w-6 text-blue-500" />,
          bgClass: "bg-blue-50 border-blue-200",
          buttonClass: "bg-blue-600 hover:bg-blue-700 text-white"
        };
      default:
        return {
          icon: <AlertTriangle className="h-6 w-6 text-red-500" />,
          bgClass: "bg-red-50 border-red-200",
          buttonClass: "bg-red-600 hover:bg-red-700 text-white"
        };
    }
  };

  const { icon, bgClass, buttonClass } = getIconAndColors();

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      {/* Backdrop */}
      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        exit={{ opacity: 0 }}
        className="absolute inset-0 bg-black/50 backdrop-blur-sm"
        onClick={onClose}
      />
      
      {/* Dialog */}
      <motion.div
        initial={{ opacity: 0, scale: 0.9, y: 20 }}
        animate={{ opacity: 1, scale: 1, y: 0 }}
        exit={{ opacity: 0, scale: 0.9, y: 20 }}
        transition={{ duration: 0.2 }}
        className={`relative w-full max-w-md mx-4 p-6 rounded-xl border shadow-2xl ${bgClass}`}
      >
        {/* Close button */}
        <button
          onClick={onClose}
          className="absolute top-4 right-4 p-1 rounded-full hover:bg-black/10 transition-colors"
        >
          <X className="h-4 w-4 text-gray-600" />
        </button>

        {/* Content */}
        <div className="flex items-start gap-4">
          {icon}
          <div className="flex-1">
            <h3 className="text-lg font-semibold text-gray-900 mb-2">
              {title}
            </h3>
            <p className="text-sm text-gray-600 mb-6">
              {message}
            </p>
            
            {/* Actions */}
            <div className="flex gap-3 justify-end">
              <button
                onClick={onClose}
                className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-lg transition-colors"
              >
                {cancelText}
              </button>
              <button
                onClick={() => {
                  onConfirm();
                  onClose();
                }}
                className={`px-4 py-2 text-sm font-medium rounded-lg transition-colors ${buttonClass}`}
              >
                {confirmText}
              </button>
            </div>
          </div>
        </div>
      </motion.div>
    </div>
  );
};

export default ConfirmDialog;
