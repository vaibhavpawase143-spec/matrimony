// Global Error Handler for API calls and application errors
class ErrorHandler {
  constructor() {
    this.setupGlobalHandlers();
    this.toast = null; // Will be set when toast is available
  }

  setToast(toastInstance) {
    this.toast = toastInstance;
  }

  setupGlobalHandlers() {
    // Handle unhandled promise rejections
    window.addEventListener('unhandledrejection', (event) => {
      console.error('Unhandled promise rejection:', event.reason);
      this.handleError(event.reason, 'Unhandled Promise Rejection');
    });

    // Handle uncaught errors
    window.addEventListener('error', (event) => {
      console.error('Uncaught error:', event.error);
      this.handleError(event.error, 'Uncaught Error');
    });
  }

  handleError(error, context = 'Application Error', showToast = true) {
    const errorInfo = {
      message: this.extractErrorMessage(error),
      status: error?.status || null,
      errorCode: error?.errorCode || null,
      context,
      timestamp: new Date().toISOString(),
      stack: error?.stack || null,
      validationErrors: error?.validationErrors || null
    };

    console.error(`🚨 ${context}:`, errorInfo);

    // Handle specific error types
    if (this.isNetworkError(error)) {
      this.handleNetworkError(error, showToast);
    } else if (this.isAuthError(error)) {
      this.handleAuthError(error, showToast);
    } else if (this.isValidationError(error)) {
      this.handleValidationError(error, showToast);
    } else if (this.isNotFoundError(error)) {
      this.handleNotFoundError(error, showToast);
    } else if (this.isConflictError(error)) {
      this.handleConflictError(error, showToast);
    } else if (this.isServerError(error)) {
      this.handleServerError(error, showToast);
    } else {
      this.handleGenericError(error, showToast);
    }

    return errorInfo;
  }

  // Extract the most appropriate error message from backend response
  extractErrorMessage(error) {
    // Priority: error.message > error.backendError > generic message
    if (error?.message) {
      return error.message;
    }
    if (error?.backendError) {
      return error.backendError;
    }
    if (error?.errorCode) {
      return this.getMessageFromErrorCode(error.errorCode);
    }
    return 'An unexpected error occurred. Please try again.';
  }

  // Get user-friendly message from error code
  getMessageFromErrorCode(errorCode) {
    const errorMessages = {
      'ERR_404': 'Resource not found',
      'ERR_400': 'Invalid request',
      'ERR_400_VALIDATION': 'Validation failed',
      'ERR_401_AUTH': 'Invalid credentials',
      'ERR_401_EXPIRED': 'Session expired. Please login again',
      'ERR_401_INVALID': 'Invalid authentication token',
      'ERR_403': 'Access denied',
      'ERR_500': 'Server error. Please try again later',
      'ERR_EMAIL_EXISTS': 'Email already registered',
      'ERR_PHONE_EXISTS': 'Phone number already registered',
      'ERR_USER_NOT_FOUND': 'User not found',
      'ERR_PROFILE_NOT_FOUND': 'Profile not found',
      'ERR_PROFILE_EXISTS': 'Profile already exists',
      'ERR_FILE_SIZE': 'File size exceeded limit',
      'ERR_INVALID_CREDENTIALS': 'Invalid email or password',
      'ERR_EMAIL_NOT_VERIFIED': 'Email not verified. Please verify your email first',
      'ERR_PHONE_NOT_VERIFIED': 'Phone not verified. Please verify your phone first',
      'ERR_ACCOUNT_BLOCKED': 'Your account has been blocked. Please contact support',
      'ERR_ACCOUNT_NOT_ACTIVE': 'Your account is not active. Please contact support',
      'ERR_TOKEN_EXPIRED': 'Token has expired. Please request a new one',
      'ERR_INVALID_TOKEN': 'Invalid token. Please request a new one',
      'ERR_NETWORK': 'No internet connection. Please check your internet connection'
    };
    return errorMessages[errorCode] || 'An error occurred';
  }

  isNetworkError(error) {
    return (
      error instanceof TypeError ||
      error.message?.includes('Failed to fetch') ||
      error.message?.includes('Network Error') ||
      error.message?.includes('ERR_NETWORK') ||
      error.message?.includes('No internet connection') ||
      error.type === 'NETWORK_ERROR' ||
      error.errorCode === 'ERR_NETWORK'
    );
  }

  isAuthError(error) {
    return (
      error?.status === 401 ||
      error?.status === 403 ||
      error?.errorCode === 'ERR_INVALID_CREDENTIALS' ||
      error?.errorCode === 'ERR_EMAIL_NOT_VERIFIED' ||
      error?.errorCode === 'ERR_PHONE_NOT_VERIFIED' ||
      error?.errorCode === 'ERR_ACCOUNT_BLOCKED' ||
      error?.errorCode === 'ERR_ACCOUNT_NOT_ACTIVE' ||
      error?.errorCode === 'ERR_TOKEN_EXPIRED' ||
      error?.errorCode === 'ERR_INVALID_TOKEN' ||
      error?.errorCode?.includes('ERR_401') ||
      error?.errorCode?.includes('ERR_403') ||
      error.message?.includes('Unauthorized') ||
      error.message?.includes('Token expired') ||
      error.message?.includes('Invalid token')
    );
  }

  isValidationError(error) {
    return (
      error?.status === 400 ||
      error?.errorCode?.includes('ERR_400') ||
      error?.backendError === 'VALIDATION_ERROR' ||
      error.message?.includes('Validation')
    );
  }

  isNotFoundError(error) {
    return (
      error?.status === 404 ||
      error?.errorCode?.includes('ERR_404') ||
      error?.backendError === 'NOT_FOUND'
    );
  }

  isConflictError(error) {
    return (
      error?.status === 409 ||
      error?.errorCode?.includes('ERR_') ||
      error?.backendError === 'CONFLICT'
    );
  }

  isServerError(error) {
    return error?.status >= 500 || error?.errorCode?.includes('ERR_500');
  }

  handleNetworkError(error, showToast = true) {
    console.warn('🌐 Network error detected:', error.message);
    const message = 'Network connection error. Please check your internet connection.';
    if (showToast && this.toast) {
      this.toast.error(message);
    }
    return { type: 'NETWORK_ERROR', message };
  }

  handleAuthError(error, showToast = true) {
    console.warn('🔐 Authentication error detected:', error.message);
    const message = error.message || 'Authentication failed. Please login again.';
    
    // Clear auth data and redirect to login
    if (typeof window !== 'undefined') {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('role');
      localStorage.removeItem('isAdmin');
      
      // Only redirect if not already on login/register page
      const currentPath = window.location.pathname;
      if (!['/login', '/register', '/'].includes(currentPath)) {
        window.location.href = '/login';
      }
    }
    
    if (showToast && this.toast) {
      this.toast.error(message);
    }
    
    return { type: 'AUTH_ERROR', message };
  }

  handleValidationError(error, showToast = true) {
    console.warn('⚠️ Validation error detected:', error.message);
    const message = error.message || 'Invalid data provided.';
    
    if (showToast && this.toast) {
      this.toast.error(message);
    }
    
    return { 
      type: 'VALIDATION_ERROR', 
      message,
      validationErrors: error.validationErrors || null
    };
  }

  handleNotFoundError(error, showToast = true) {
    console.warn('🔍 Not found error detected:', error.message);
    const message = error.message || 'Resource not found.';
    
    if (showToast && this.toast) {
      this.toast.error(message);
    }
    
    return { type: 'NOT_FOUND_ERROR', message };
  }

  handleConflictError(error, showToast = true) {
    console.warn('⚔️ Conflict error detected:', error.message);
    const message = error.message || 'Resource conflict occurred.';
    
    if (showToast && this.toast) {
      this.toast.error(message);
    }
    
    return { type: 'CONFLICT_ERROR', message };
  }

  handleServerError(error, showToast = true) {
    console.warn('💥 Server error detected:', error.message);
    const message = error.message || 'Server error. Please try again later.';
    
    if (showToast && this.toast) {
      this.toast.error(message);
    }
    
    return { type: 'SERVER_ERROR', message };
  }

  handleGenericError(error, showToast = true) {
    console.warn('❌ Generic error detected:', error.message);
    const message = error.message || 'An unexpected error occurred.';
    
    if (showToast && this.toast) {
      this.toast.error(message);
    }
    
    return { type: 'GENERIC_ERROR', message };
  }

  // Static method for easy access
  static handle(error, context = 'Application Error', showToast = true) {
    const handler = new ErrorHandler();
    return handler.handleError(error, context, showToast);
  }
}

// Create singleton instance
const errorHandler = new ErrorHandler();

export default errorHandler;
