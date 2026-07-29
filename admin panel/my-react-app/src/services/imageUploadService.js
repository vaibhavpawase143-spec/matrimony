const API_BASE_URL = "/api";

export async function uploadImage(file) {
  const token = localStorage.getItem("adminToken");

  const formData = new FormData();
  formData.append("file", file);

  const response = await fetch(
    `${API_BASE_URL}/image/upload`,
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: formData,
    }
  );

  if (!response.ok) {
    const error = await response.text();
    throw new Error(error || "Image upload failed");
  }

  return response.text();
}