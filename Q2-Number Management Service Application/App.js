const express = require("express");
const fetch = require("node-fetch");

const app = express();
const port = 3000;

// Function to fetch and merge data from multiple URLs
async function fetchDataFromURLs(urls) {
  const promises = urls.map((url) =>
    fetch(url)
      .then((response) => response.json())
      .then((data) => data.numbers)
      .catch((err) => [])
  );

  const results = await Promise.allSettled(promises);

  const mergedNumbers = results.reduce((merged, result) => {
    if (result.status === "fulfilled" && Array.isArray(result.value)) {
      merged = merged.concat(result.value);
    }
    return merged;
  }, []);

  return Array.from(new Set(mergedNumbers)).sort((a, b) => a - b);
}

// API endpoint for /numbers
app.get("/numbers", async (req, res) => {
  const { url } = req.query;

  if (!url) {
    return res.status(400).json({ error: "No URLs provided" });
  }

  const urls = Array.isArray(url) ? url : [url];

  try {
    const mergedNumbers = await fetchDataFromURLs(urls);
    return res.json({ numbers: mergedNumbers });
  } catch (err) {
    return res.status(500).json({ error: "Error fetching data" });
  }
});

app.listen(port, () => {
  console.log(`Number Management HTTP microservice listening on port ${port}!`);
});
