export type Variant = {
  id: number;
  itemId: number;
  itemName: string;
  name: string;
  sku: string;
  price: number;
  stock: number;
};

export type Item = {
  id: number;
  name: string;
  description: string | null;
  variants: Variant[];
};

const baseUrl = (process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080").replace(/\/$/, "");

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${baseUrl}${path}`, {
    ...init,
    headers: { "Content-Type": "application/json", ...(init?.headers || {}) },
    cache: "no-store"
  });
  if (!response.ok) {
    const error = await response.json().catch(() => null);
    throw new Error(error?.message || `Request failed (${response.status})`);
  }
  return response.status === 204 ? (undefined as T) : response.json();
}

export const api = {
  listItems: () => request<Item[]>("/api/items"),
  createItem: (payload: { name: string; description: string }) => request<Item>("/api/items", { method: "POST", body: JSON.stringify(payload) }),
  adjustStock: (id: number, quantity: number) => request<Variant>(`/api/variants/${id}/stock-adjustments`, { method: "POST", body: JSON.stringify({ quantity, reason: "Dashboard adjustment" }) }),
  sell: (id: number, quantity: number) => request<Variant>(`/api/variants/${id}/sales`, { method: "POST", body: JSON.stringify({ quantity, reason: "Dashboard sale" }) })
};
