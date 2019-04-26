import { applyMiddleware, createStore } from "redux";
import reducers from "./reducers";
import thunk from "redux-thunk";
import { createLogger } from "redux-logger";

const logger = createLogger({
  // ...options
});

const store = createStore(
  reducers,
  applyMiddleware(thunk, logger),
  typeof window === "object" &&
  typeof window.devToolsExtension !== "undefined"
    ? window.devToolsExtension()
    : f => f
);

export default store;