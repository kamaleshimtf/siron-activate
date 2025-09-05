This is the front-end for the Siron Activate project. It is built using Next.js and TypeScript.

## Getting Started

You can simply build the project with docker by running the following command:

```bash
docker build -t siron-activate-fe-image .
```

Then, you can run the docker container with the following command:
```
docker run -p 3000:3000 \
-e AUTH_SECRET=secret \
-e AUTH_OIDC_ISSUER=issuer \
-e AUTH_OIDC_CLIENT_ID=client_id \
-e AUTH_OIDC_CLIENT_SECRET=secret \
-e AUTH_TRUST_HOST=true \
-e API_URL=api_url \
--name siron-activate-fe siron-activate-fe-image
```

## Development

First, create a `.env.local` file in the root of the project and set the variables defined in the `.env` file.

Then, run the development server:

```bash
npm install
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the result.
