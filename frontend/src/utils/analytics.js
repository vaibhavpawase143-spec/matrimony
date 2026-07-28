import ReactGA from "react-ga4";

const measurementId = import.meta.env.VITE_GA_MEASUREMENT_ID;

export const initGA = () => {
  if (!measurementId) {
    console.warn("GA Measurement ID missing.");
    return;
  }

  ReactGA.initialize(measurementId);
};

export const trackPageView = (path) => {
  ReactGA.send({
    hitType: "pageview",
    page: path,
  });
};

export const trackEvent = (eventName, parameters = {}) => {
  ReactGA.event(eventName, parameters);
};