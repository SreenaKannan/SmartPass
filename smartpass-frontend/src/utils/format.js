export const formatINR = (value) =>
    "\u20B9" + Number(value || 0).toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });

export const formatDateTime = (value) => {
    if (!value) return "-";
    const d = new Date(value);
    return Number.isNaN(d.getTime())
        ? value
        : d.toLocaleString("en-IN", { day: "2-digit", month: "short", year: "numeric", hour: "2-digit", minute: "2-digit" });
};

export const formatDate = (value) => {
    if (!value) return "-";
    const d = new Date(value);
    return Number.isNaN(d.getTime())
        ? value
        : d.toLocaleDateString("en-IN", { day: "2-digit", month: "short", year: "numeric" });
};

export const STATUS_STYLE = {
    SUCCESS: { label: "Successful", cls: "bg-success" },
    FAILED: { label: "Failed", cls: "bg-danger" },
    CANCELLED: { label: "Cancelled", cls: "bg-secondary" },
    EXPIRED: { label: "Expired", cls: "bg-secondary" },
    INITIATED: { label: "Awaiting payment", cls: "bg-warning text-dark" },
};

export const purposeLabel = (purpose) =>
    purpose === "renewal" ? "Pass renewal" : "Application fee";
