# GELI Warehouse Management System

Warehouse inventory API and dashboard for the Great Eastern Life Indonesia Java Backend Developer technical assessment.

## Architecture

This is a small monorepo with two independently deployable services:

- `backend/`: Java 21 + Spring Boot 3.4 REST API, packaged with Docker for Render.
- `frontend/`: Next.js + TypeScript dashboard for Vercel.
- H2 is the zero-configuration local database. The backend also accepts a PostgreSQL JDBC URL for production.

The backend owns all inventory rules. A sale is a transaction that locks the variant row and rejects the request when the requested quantity exceeds available stock.

## Run locally

### Backend

```bash
cd backend
./mvnw spring-boot:run       # macOS/Linux
./mvnw.cmd spring-boot:run   # Windows PowerShell
```

The API runs at `http://localhost:8080`. Swagger UI is available at `http://localhost:8080/swagger-ui.html` and the health check is `http://localhost:8080/api/health`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:3000`. The default API URL is `http://localhost:8080`; set `NEXT_PUBLIC_API_BASE_URL` when the API is hosted elsewhere.

## API examples

```bash
# List items with variants
curl http://localhost:8080/api/items

# Create an item
curl -X POST http://localhost:8080/api/items -H "Content-Type: application/json" \
  -d '{"name":"Canvas Tote","description":"Reusable cotton tote"}'

# Sell three units. A 400 response is returned when stock is insufficient.
curl -X POST http://localhost:8080/api/variants/1/sales -H "Content-Type: application/json" \
  -d '{"quantity":3,"reason":"order-1001"}'

# Add stock
curl -X POST http://localhost:8080/api/variants/1/stock-adjustments -H "Content-Type: application/json" \
  -d '{"quantity":10,"reason":"restock"}'
```

## Deploy

### Backend on Render

1. Create a Render Web Service from this repository.
2. Select Docker and set the Dockerfile path to `backend/Dockerfile` with `backend` as the Docker context. The included `render.yaml` can be used as a Blueprint.
3. Set `FRONTEND_URL` to the deployed Vercel URL.
4. For persistent production data, set `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, and `SPRING_DATASOURCE_DRIVER=org.postgresql.Driver` to a PostgreSQL database.
5. Keep `DDL_AUTO=update` for the first deployment, or change it to the migration strategy used by the database.

Render exposes the service URL, for example `https://geli-warehouse-api.onrender.com`.

### Frontend on Vercel

1. Import the repository as a Vercel project.
2. Set the project Root Directory to `frontend`.
3. Set `NEXT_PUBLIC_API_BASE_URL` to the Render backend URL.
4. Deploy with the default Next.js build command.

## Verification

```bash
cd backend
./mvnw test

cd ../frontend
npm install
npm run typecheck
npm run lint
npm run build
```

## Design decisions and assumptions

- Items are catalog products; variants carry SKU, price, and stock.
- SKU is globally unique and enforced at both the service and database layers.
- Stock can never become negative. Stock mutations are transactional and use a pessimistic row lock to avoid overselling under concurrent requests.
- Authentication and order history are outside the assessment scope.
- H2 is intentionally the local default so a reviewer can run the project with one command. Render should use PostgreSQL for durable data.
