export const hasPermission = (role, module) => {
    return PERMISSIONS[role]?.[module] || false;
}