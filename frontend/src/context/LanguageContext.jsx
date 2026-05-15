import { createContext, useContext, useEffect, useMemo, useState } from "react";
import translations from "../translations";

const DEFAULT_LANGUAGE = "en";
const STORAGE_KEY = "gathbandhanLanguage";

const LanguageContext = createContext({
  language: DEFAULT_LANGUAGE,
  setLanguage: () => {},
  t: translations[DEFAULT_LANGUAGE],
});

export const LanguageProvider = ({ children }) => {
  const [language, setLanguage] = useState(DEFAULT_LANGUAGE);

  useEffect(() => {
    const savedLanguage = localStorage.getItem(STORAGE_KEY);
    if (savedLanguage && translations[savedLanguage]) {
      setLanguage(savedLanguage);
      return;
    }

    const browserLanguage = navigator.language?.split("-")[0];
    if (browserLanguage && translations[browserLanguage]) {
      setLanguage(browserLanguage);
    }
  }, []);

  useEffect(() => {
    localStorage.setItem(STORAGE_KEY, language);
  }, [language]);

  const contextValue = useMemo(
    () => ({
      language,
      setLanguage,
      t: translations[language] || translations[DEFAULT_LANGUAGE],
    }),
    [language]
  );

  return (
    <LanguageContext.Provider value={contextValue}>
      {children}
    </LanguageContext.Provider>
  );
};

export const useLanguage = () => useContext(LanguageContext);
