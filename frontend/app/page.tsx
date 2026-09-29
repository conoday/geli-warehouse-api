"use client";

import { FormEvent, useEffect, useMemo, useState } from "react";
import { api, Item, Variant } from "../lib/api";

const currency = new Intl.NumberFormat("id-ID", { style: "currency", currency: "IDR", maximumFractionDigits: 0 });

export default function Dashboard() {
  const [items, setItems] = useState<Item[]>([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [newItem, setNewItem] = useState({ name: "", description: "" });

  const variants = useMemo(() => items.flatMap((item) => item.variants), [items]);
  const totalStock = variants.reduce((total, variant) => total + variant.stock, 0);

  async function loadItems() {
    setLoading(true);
    setError("");
    try { setItems(await api.listItems()); }
    catch (err) { setError(err instanceof Error ? err.message : "Could not connect to the API"); }
    finally { setLoading(false); }
  }

  useEffect(() => { void loadItems(); }, []);

  async function createItem(event: FormEvent) {
    event.preventDefault();
    if (!newItem.name.trim()) return;
    try {
      await api.createItem(newItem);
      setNewItem({ name: "", description: "" });
      setMessage("Item created successfully.");
      await loadItems();
    } catch (err) { setError(err instanceof Error ? err.message : "Could not create item"); }
  }

  async function changeStock(variant: Variant, action: "add" | "sell") {
    const input = window.prompt(`${action === "add" ? "Add" : "Sell"} quantity for ${variant.sku}`, "1");
    const quantity = Number(input);
    if (!Number.isInteger(quantity) || quantity < 1) return;
    try {
      if (action === "add") await api.adjustStock(variant.id, quantity);
      else await api.sell(variant.id, quantity);
      setMessage(`${action === "add" ? "Stock added" : "Sale recorded"} for ${variant.sku}.`);
      await loadItems();
    } catch (err) { setError(err instanceof Error ? err.message : "Stock operation failed"); }
  }

  return (
    <main className="shell">
      <header className="hero">
        <div><span className="eyebrow">GELI / WAREHOUSE</span><h1>Inventory, at a glance.</h1><p>Track variants, pricing, and stock movements from one small, focused dashboard.</p></div>
        <button className="secondary" onClick={() => void loadItems()} disabled={loading}>↻ Refresh</button>
      </header>

      <section className="stats">
        <div><span>Items</span><strong>{items.length}</strong></div>
        <div><span>Variants</span><strong>{variants.length}</strong></div>
        <div><span>Units in stock</span><strong>{totalStock}</strong></div>
        <div><span>API status</span><strong className={error ? "offline" : "online"}>{error ? "Offline" : "Online"}</strong></div>
      </section>

      {message && <div className="notice success">{message}<button onClick={() => setMessage("")}>×</button></div>}
      {error && <div className="notice error">{error}<button onClick={() => setError("")}>×</button></div>}

      <section className="content-grid">
        <div className="panel inventory-panel">
          <div className="panel-heading"><div><span className="eyebrow">LIVE CATALOG</span><h2>Inventory</h2></div><span className="count">{variants.length} variants</span></div>
          {loading ? <div className="empty">Loading inventory…</div> : items.length === 0 ? <div className="empty">No items yet. Add the first item beside this panel.</div> : <div className="item-list">
            {items.map((item) => <article className="item-card" key={item.id}><div className="item-title"><div><h3>{item.name}</h3><p>{item.description || "No description"}</p></div><span>{item.variants.length} variants</span></div>
              <div className="variant-table"><div className="table-head"><span>Variant</span><span>Price</span><span>Stock</span><span>Actions</span></div>
                {item.variants.map((variant) => <div className="table-row" key={variant.id}><div><strong>{variant.name}</strong><small>{variant.sku}</small></div><span>{currency.format(variant.price)}</span><span className={variant.stock === 0 ? "stock empty-stock" : variant.stock < 5 ? "stock low-stock" : "stock"}>{variant.stock} units</span><div className="actions"><button onClick={() => void changeStock(variant, "add")}>+ Stock</button><button className="sell" onClick={() => void changeStock(variant, "sell")} disabled={variant.stock === 0}>Sell</button></div></div>)}
              </div></article>)}
          </div>}
        </div>

        <aside className="panel add-panel"><span className="eyebrow">QUICK ACTION</span><h2>Add an item</h2><p>Create a catalog item first, then add its variants through the API.</p><form onSubmit={createItem}><label>Name<input value={newItem.name} onChange={(event) => setNewItem({ ...newItem, name: event.target.value })} placeholder="e.g. Classic T-Shirt" required /></label><label>Description<textarea value={newItem.description} onChange={(event) => setNewItem({ ...newItem, description: event.target.value })} placeholder="Short product description" rows={4} /></label><button className="primary" type="submit">Create item <span>→</span></button></form><div className="api-note"><span className="dot" /> Connected to configured warehouse API</div></aside>
      </section>
      <footer>GELI Warehouse Management <span>•</span> Spring Boot API + Next.js dashboard</footer>
    </main>
  );
}
