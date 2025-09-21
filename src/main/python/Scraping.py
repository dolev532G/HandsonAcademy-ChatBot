import requests
from bs4 import BeautifulSoup

def search_imdb(keyword="suparman"):
    url = f"https://www.imdb.com/find/?q={keyword}"

    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                      "AppleWebKit/537.36 (KHTML, like Gecko) "
                      "Chrome/140.0.0.0 Safari/537.36",
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
        "Accept-Language": "en-US,en;q=0.5",
        "Connection": "keep-alive",
        "Upgrade-Insecure-Requests": "1"
    }

    response = requests.get(url, headers=headers)
    response.raise_for_status()

    # Save HTML for inspection
    with open("test.html", "w", encoding="utf-8") as f:
        f.write(response.text)

    soup = BeautifulSoup(response.text, "lxml")

    # Find all movie <li> elements
    movie_items = soup.select("li.find-result-item.find-title-result")
    print(f"Found {len(movie_items)} movies for '{keyword}':\n")

    for li in movie_items:
        # Title
        title_tag = li.select_one("a.ipc-metadata-list-summary-item__t")
        title = title_tag.get_text(strip=True) if title_tag else "N/A"

        # Year / Type (first ul)
        year_tag = li.select_one("ul.ipc-inline-list.ipc-inline-list--show-dividers.ipc-inline-list--no-wrap.ipc-inline-list--inline.ipc-metadata-list-summary-item__tl li span")
        year = year_tag.get_text(strip=True) if year_tag else "N/A"

        # Actors (second ul)
        actors_tag = li.select_one("ul.ipc-inline-list.ipc-inline-list--show-dividers.ipc-inline-list--no-wrap.ipc-inline-list--inline.ipc-metadata-list-summary-item__stl li span")
        actors = actors_tag.get_text(strip=True) if actors_tag else "No actors listed"

        print(f"🎬 {title} ({year})")
        print(f"   ⭐ Actors: {actors}\n")


if __name__ == "__main__":
    search_imdb()
