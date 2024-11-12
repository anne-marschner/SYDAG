# Step 1: Build the Next.js app with Node.js
FROM node:latest

# Set working directory
WORKDIR /app

# Install dependencies
COPY package.json package-lock.json ./
RUN npm install

# Copy the rest of the app
COPY . .

# Expose port 3000 (the default port for Next.js)
EXPOSE 3000

# Start the Next.js development server (with hot-reload)
CMD ["npm", "run", "dev"]
