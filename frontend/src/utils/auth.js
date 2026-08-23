export const isAdmin = () => {
  return (sessionStorage.getItem("role") || localStorage.getItem("role")) === "ADMIN";
};