// Global Error Handler for API calls and application errors
class ErrorHandler {
  constructor() {
    this.setupGlobalHandlers();
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

  handleError(error, context = 'Application Error') {
    const errorInfo = {
      message: error?.message || 'Unknown error occurred',
      status: error?.status || null,
      context,
      timestamp: new Date().toISOString(),
      stack: error?.stack || null
    };

    console.error(`🚨 ${context}:`, errorInfo);

    // Handle specific error types
    if (this.isNetworkError(error)) {
      this.handleNetworkError(error);
    } else if (this.isAuthError(error)) {
      this.handleAuthError(error);
    } else if (this.isValidationError(error)) {
      this.handleValidationError(error);
    } else {
      this.handleGenericError(error);
    }

    return errorInfo;
  }

  isNetworkError(error) {
    return (
      error instanceof TypeError ||
      error.message?.includes('Failed to fetch') ||
      error.message?.includes('Network Error') ||
      error.message?.includes('ERR_NETWORK')
    );
  }

  isAuthError(error) {
    return (
      error?.status === 401 ||
      error?.status === 403 ||
      error.message?.includes('Unauthorized') ||
      error.message?.includes('Token expired') ||
      error.message?.includes('Invalid token')
    );
  }

  isValidationError(error) {
    return error?.status === 400 || error.message?.includes('Validation');
  }

  handleNetworkError(error) {
    console.warn('🌐 Network error detected:', error.message);
    // Could show a toast notification here
    return { type: 'NETWORK_ERROR', message: 'Network connection error. Please check your internet connection.' };
  }

  handleAuthError(error) {
    console.warn('🔐 Authentication error detected:', error.message);
    
    // Clear auth data and redirect to login
    if (typeof window !== 'undefined') {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('role');
      
      // Only redirect if not already on login/register page
      const currentPath = window.location.pathname;
      if (!['/login', '/register', '/'].includes(currentPath)) {
        window.location.href = '/login';
      }
    }
    
    return { type: 'AUTH_ERROR', message: 'Authentication failed. Please login again.' };
  }

  handleValidationError(error) {
    console.warn('⚠️ Validation error detected:', error.message);
    return { type: 'VALIDATION_ERROR', message: error.message || 'Invalid data provided.' };
  }

  handleGenericError(error) {
    console.warn('❌ Generic error detected:', error.message);
    return { type: 'GENERIC_ERROR', message: error.message || 'An unexpected error occurred.' };
  }

  // Static method for easy access
  static handle(error, context = 'Application Error') {
    const handler = new ErrorHandler();
    return handler.handleError(error, context);
  }
}

// Create singleton instance
const errorHandler = new ErrorHandler();

export default errorHandler;
