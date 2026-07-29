import React from "react";
import { X } from "lucide-react";

const formatDate = (date) => {
    if (!date) return "-";

    return new Date(date).toLocaleString();
};

const Field = ({ label, value }) => (
    <div className="border-b pb-3">
        <p className="text-xs font-medium text-gray-500 uppercase mb-1">
            {label}
        </p>
        <p className="text-sm text-gray-800 break-words">
            {value || "-"}
        </p>
    </div>
);

const AuditLogDetailsModal = ({ open, onClose, auditLog }) => {

    if (!open || !auditLog) return null;

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
            <div className="bg-white rounded-xl shadow-xl w-full max-w-4xl max-h-[90vh] overflow-y-auto">

                <div className="flex items-center justify-between border-b px-6 py-4">
                    <h2 className="text-xl font-semibold">
                        Audit Log Details
                    </h2>

                    <button
                        onClick={onClose}
                        className="p-2 rounded hover:bg-gray-100"
                    >
                        <X size={20} />
                    </button>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6 p-6">

                    <Field
                        label="ID"
                        value={auditLog.id}
                    />

                    <Field
                        label="Admin"
                        value={auditLog.adminName}
                    />

                    <Field
                        label="Module"
                        value={auditLog.module}
                    />

                    <Field
                        label="Action"
                        value={auditLog.action}
                    />

                    <Field
                        label="Entity Type"
                        value={auditLog.entityType}
                    />

                    <Field
                        label="Entity ID"
                        value={auditLog.entityId}
                    />

                    <Field
                        label="Description"
                        value={auditLog.description}
                    />

                    <Field
                        label="IP Address"
                        value={auditLog.ipAddress}
                    />

                    <Field
                        label="User Agent"
                        value={auditLog.userAgent}
                    />

                    <Field
                        label="Created At"
                        value={formatDate(auditLog.createdAt)}
                    />

                </div>

                <div className="px-6 pb-6 space-y-5">

                    <div>
                        <h3 className="font-semibold mb-2">
                            Old Value
                        </h3>

                        <pre className="bg-gray-100 rounded p-4 text-sm whitespace-pre-wrap overflow-auto">
{auditLog.oldValue || "-"}
                        </pre>
                    </div>

                    <div>
                        <h3 className="font-semibold mb-2">
                            New Value
                        </h3>

                        <pre className="bg-gray-100 rounded p-4 text-sm whitespace-pre-wrap overflow-auto">
{auditLog.newValue || "-"}
                        </pre>
                    </div>

                </div>

            </div>
        </div>
    );
};

export default AuditLogDetailsModal;