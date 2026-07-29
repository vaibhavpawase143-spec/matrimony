import { useEffect, useMemo, useState } from "react";
import { X, Loader2 } from "lucide-react";
import { toast } from "sonner";
import {
  updateAdmin,
  getRoles,
} from "../../services/adminManagementService";
export default function ChangeRoleModal({
  open,
  admin,
  onClose,
  onSuccess,
}) {
// ==========================================
// INITIAL FORM
// ==========================================

const initialForm = useMemo(
  () => ({
    roleId: admin?.roleId || "",
  }),
  [admin]
);

// ==========================================
// STATE
// ==========================================

const [form, setForm] = useState(initialForm);

const [roles, setRoles] = useState([]);

const [errors, setErrors] = useState({});

const [saving, setSaving] = useState(false);

// ==========================================
// RESET FORM
// ==========================================
// ==========================================
// VALIDATION
// ==========================================

const validate = () => {
  const validationErrors = {};

  if (!form.roleId) {
    validationErrors.roleId = "Please select a role.";
  }

  if (Number(form.roleId) === Number(admin.roleId)) {
    validationErrors.roleId =
      "Please select a different role.";
  }

  setErrors(validationErrors);

  return Object.keys(validationErrors).length === 0;
};
useEffect(() => {
  if (open) {
    setForm(initialForm);
    setErrors({});
  }
}, [open, initialForm]);
// ==========================================
// LOAD ROLES
// ==========================================

useEffect(() => {
  if (!open) return;

  const loadRoles = async () => {
    try {
      const response = await getRoles();

      setRoles(response.data || []);
    } catch (error) {
      console.error(error);

      toast.error("Failed to load roles.");
    }
  };

  loadRoles();
}, [open]);
// ==========================================
// HANDLE CHANGE
// ==========================================

const handleChange = (e) => {
  const { name, value } = e.target;

  setForm((prev) => ({
    ...prev,
    [name]: value,
  }));

  if (errors[name]) {
    setErrors((prev) => ({
      ...prev,
      [name]: "",
    }));
  }
};
// ==========================================
// SUBMIT
// ==========================================

const handleSubmit = async (e) => {
  e.preventDefault();

  if (!validate()) return;

  try {
    setSaving(true);

    await updateAdmin(admin.id, {
      name: admin.name,
      phone: admin.phone,
      profilePhoto: admin.profilePhoto,
      roleId: Number(form.roleId),
    });

    toast.success("Admin role updated successfully.");

    onSuccess?.();
  } catch (error) {
    console.error(error);

    toast.error(
      error?.message || "Failed to update role."
    );
  } finally {
    setSaving(false);
  }
};
if (!open) {
  return null;
}
return (
  <div
    className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 px-4"
    onClick={onClose}
  >
    <div
      className="w-full max-w-md rounded-xl bg-white shadow-2xl"
      onClick={(e) => e.stopPropagation()}
    >
      {/* Header */}

      <div className="flex items-center justify-between border-b px-6 py-4">
        <div>
          <h2 className="text-xl font-bold text-gray-800">
            Change Role
          </h2>

          <p className="mt-1 text-sm text-gray-500">
            Update administrator role
          </p>
        </div>

    <button
      type="button"
      onClick={onClose}
      disabled={saving}
          className="rounded-lg p-2 hover:bg-gray-100"
        >
          <X size={20} />
        </button>
      </div>

      <form onSubmit={handleSubmit}>
        <div className="space-y-5 px-6 py-6">

          {/* Current Role */}

          <div>
            <label className="mb-2 block text-sm font-semibold text-gray-700">
              Current Role
            </label>

            <input
              type="text"
             value={
               admin?.role === "ROLE_SUPER_ADMIN"
                 ? "Super Admin"
                 : admin?.role === "ROLE_ADMIN"
                 ? "Admin"
                 : admin?.role === "ROLE_USER"
                 ? "User"
                 : ""
             }
              disabled
              className="w-full rounded-lg border border-gray-300 bg-gray-100 px-4 py-3"
            />
          </div>

          {/* New Role */}

          <div>
            <label className="mb-2 block text-sm font-semibold text-gray-700">
              New Role
            </label>

            <select
              name="roleId"
              value={form.roleId}
              onChange={handleChange}
              className={`w-full rounded-lg border px-4 py-3 ${
                errors.roleId
                  ? "border-red-500"
                  : "border-gray-300"
              }`}
            >
              <option value="">
                Select Role
              </option>

              {roles.map((role) => (
                <option
                  key={role.id}
                  value={role.id}
                >
                  {role.name}
                </option>
              ))}
            </select>

            {errors.roleId && (
              <p className="mt-1 text-sm text-red-600">
                {errors.roleId}
              </p>
            )}
          </div>

        </div>

        {/* Footer */}

        <div className="flex justify-end gap-3 border-t px-6 py-4">

          <button
            type="button"
         onClick={() => {
           if (!saving) {
             onClose();
           }
         }}
            className="rounded-lg border border-gray-300 px-5 py-2 hover:bg-gray-100"
          >
            Cancel
          </button>

          <button
            type="submit"
            disabled={saving}
            className="flex min-w-[140px] items-center justify-center rounded-lg bg-violet-700 px-5 py-2 text-white hover:bg-violet-800 disabled:opacity-60"
          >
            {saving ? (
              <>
                <Loader2
                  size={18}
                  className="mr-2 animate-spin"
                />
                Updating...
              </>
            ) : (
              "Update Role"
            )}
          </button>

        </div>
      </form>
    </div>
  </div>
);
}