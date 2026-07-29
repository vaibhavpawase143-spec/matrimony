import { useEffect, useMemo, useState } from "react";
import { X, Loader2 } from "lucide-react";
import { toast } from "sonner";

import {
  updateAdmin,
  uploadAdminPhoto,
} from "../../services/adminManagementService";

import { IMAGE_BASE_URL } from "../../services/api";

export default function EditAdminModal({
  open,
  admin,
  onClose,
  onSuccess,
}) {
  // ==========================================
  // INITIAL FORM
  // ==========================================
const [selectedPhoto, setSelectedPhoto] = useState(null);
const handlePhotoChange = (e) => {
  const file = e.target.files?.[0];

  if (!file) return;

  // Only images
  if (!file.type.startsWith("image/")) {
    toast.error("Please select a valid image.");
    return;
  }

  // Max 5 MB
  if (file.size > 5 * 1024 * 1024) {
    toast.error("Image size must be less than 5 MB.");
    return;
  }

  setSelectedPhoto(file);
};
  const initialForm = useMemo(
    () => ({
      name: admin?.name || "",
      phone: admin?.phone || "",
      profilePhoto: admin?.profilePhoto || "",
    }),
    [admin]
  );

  // ==========================================
  // STATE
  // ==========================================

  const [form, setForm] = useState(initialForm);

  const [errors, setErrors] = useState({});

  const [saving, setSaving] = useState(false);

  // ==========================================
  // RESET FORM WHEN MODAL OPENS
  // ==========================================

useEffect(() => {
  if (open) {
    setForm(initialForm);
    setErrors({});
    setSelectedPhoto(null);
  }
}, [open, initialForm]);

 // ==========================================
 // CLOSE ON ESCAPE
 // ==========================================

 useEffect(() => {
   if (!open) return;

   const handleEscape = (event) => {
     if (event.key === "Escape" && !saving) {
       onClose();
     }
   };

   document.addEventListener("keydown", handleEscape);

   return () => {
     document.removeEventListener("keydown", handleEscape);
   };
 }, [open, saving, onClose]);

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
 // VALIDATION
 // ==========================================

 const validate = () => {
   const validationErrors = {};

   if (!form.name.trim()) {
     validationErrors.name = "Name is required.";
   } else if (form.name.trim().length < 3) {
     validationErrors.name =
       "Name must be at least 3 characters.";
   }

   if (
     form.phone &&
     !/^[0-9]{10}$/.test(form.phone)
   ) {
     validationErrors.phone =
       "Phone number must contain exactly 10 digits.";
   }

   setErrors(validationErrors);

   return Object.keys(validationErrors).length === 0;
 };

 // ==========================================
 // SUBMIT
 // ==========================================

const handleSubmit = async (e) => {
  e.preventDefault();

  if (!validate()) return;

  try {
    setSaving(true);

    // ==========================================
    // Existing photo path
    // ==========================================
    let profilePhoto = form.profilePhoto;

    // ==========================================
    // Upload new photo if selected
    // ==========================================
    if (selectedPhoto) {
      const uploadResponse = await uploadAdminPhoto(
        admin.id,
        selectedPhoto
      );

      profilePhoto = uploadResponse.data;
    }

    // ==========================================
    // Update admin details
    // ==========================================
    await updateAdmin(admin.id, {
      name: form.name.trim(),
      phone: form.phone.trim(),
      profilePhoto: profilePhoto,
    });

    toast.success("Admin updated successfully.");

    onSuccess?.();
  } catch (error) {
    console.error(error);

    toast.error(
      error?.message || "Failed to update admin."
    );
  } finally {
    setSaving(false);
  }
};

 // ==========================================
 // DON'T RENDER IF CLOSED
 // ==========================================

 if (!open || !admin) return null;
 return (
   <div
     className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 px-4"
     onClick={() => {
       if (!saving) {
         onClose();
       }
     }}
   >
     <div
       className="w-full max-w-xl rounded-xl bg-white shadow-2xl"
       onClick={(e) => e.stopPropagation()}
     >
       {/* ================= HEADER ================= */}

       <div className="flex items-center justify-between border-b px-6 py-4">
         <div>
           <h2 className="text-xl font-bold text-gray-800">
             Edit Admin
           </h2>

           <p className="mt-1 text-sm text-gray-500">
             Update administrator details
           </p>
         </div>

         <button
           type="button"
           onClick={onClose}
           disabled={saving}
           className="rounded-lg p-2 hover:bg-gray-100 transition"
         >
           <X size={20} />
         </button>
       </div>

       {/* ================= FORM ================= */}

       <form onSubmit={handleSubmit}>
         <div className="space-y-5 px-6 py-6">

           {/* Name */}

           <div>
             <label className="mb-2 block text-sm font-semibold text-gray-700">
               Name
             </label>

             <input
               type="text"
               name="name"
               value={form.name}
               onChange={handleChange}
               className={`w-full rounded-lg border px-4 py-3 outline-none ${
                 errors.name
                   ? "border-red-500"
                   : "border-gray-300 focus:border-violet-500"
               }`}
             />

             {errors.name && (
               <p className="mt-1 text-sm text-red-600">
                 {errors.name}
               </p>
             )}
           </div>

           {/* Phone */}

           <div>
             <label className="mb-2 block text-sm font-semibold text-gray-700">
               Phone
             </label>

             <input
               type="text"
               name="phone"
               value={form.phone}
               maxLength={10}
               onChange={handleChange}
               className={`w-full rounded-lg border px-4 py-3 outline-none ${
                 errors.phone
                   ? "border-red-500"
                   : "border-gray-300 focus:border-violet-500"
               }`}
             />

             {errors.phone && (
               <p className="mt-1 text-sm text-red-600">
                 {errors.phone}
               </p>
             )}
           </div>
{/* Profile Photo */}
<div>
  <label className="mb-2 block text-sm font-semibold text-gray-700">
    Profile Photo
  </label>

  {/* Current / Preview Photo */}
  <div className="mb-4">
    <img
      src={
        selectedPhoto
          ? URL.createObjectURL(selectedPhoto)
          : form.profilePhoto
          ? form.profilePhoto.startsWith("http")
            ? form.profilePhoto
            : `${IMAGE_BASE_URL}${form.profilePhoto}`
          : "/default-avatar.png"
      }
      alt="Profile"
      className="w-24 h-24 rounded-full object-cover border"
    />
  </div>

  {/* File Upload */}
  <input
    type="file"
    accept="image/*"
    onChange={handlePhotoChange}
    className="block w-full rounded-lg border border-gray-300 p-2 text-sm"
  />

  <p className="mt-2 text-xs text-gray-500">
    Supported formats: JPG, JPEG, PNG (Max 5 MB)
  </p>
</div>
</div>
         {/* ================= FOOTER ================= */}

         <div className="flex justify-end gap-3 border-t px-6 py-4">

           <button
             type="button"
             onClick={onClose}
             disabled={saving}
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
                 Saving...
               </>
             ) : (
               "Save Changes"
             )}
           </button>

         </div>
       </form>
     </div>
   </div>
 );
 }