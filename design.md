# Design System Strategy: Ultra-Glossy Premium

## 1. Overview & Creative North Star
**Creative North Star: "The Ethereal Obsidian"**

This design system is a masterclass in depth, luminance, and digital craftsmanship. It moves away from the flat, clinical nature of standard mobile interfaces toward a "Digital Curator" aesthetic. By leveraging high-fidelity translucency and vibrant "bloom" effects against a near-black void, we create an environment that feels both expansive and intimate.

The system breaks the rigid "template" look through **intentional depth stacking**. We do not use lines to define space; we use light and shadow to manifest it. Elements don't just sit on the screen—they float in a pressurized, atmospheric space, utilizing large radii and generous breathing room to signal premium quality.

---

## 2. Colors & Surface Architecture

### The Palette
The core of this system is the contrast between the infinite depth of the background and the electric energy of the interactive accents.

*   **Foundation:** `background` (#0e0e10) serves as the true-dark canvas.
*   **Accents:** `primary` (Vibrant Purple), `secondary` (Teal), and `tertiary` (Pink) are used sparingly for critical feedback loops and brand moments.
*   **Surface Hierarchy:** We utilize five tiers of container colors to establish importance.

### The "No-Line" Rule
**Explicit Instruction:** Prohibit the use of 1px solid borders for sectioning or containment. Boundaries must be defined solely through background color shifts. 
*   Place a `surface-container-low` section against the `surface` background to create a subtle region.
*   Use `surface-container-highest` for the most prominent interactive cards to simulate a "lift" toward the user.

### Glassmorphism & Tonal Nesting
To achieve the "Ultra-Glossy" requirement, apply **Backdrop Blur (20px - 40px)** to elements using semi-transparent variations of `surface-container`. 
*   **The Signature Glow:** Main CTAs should not be flat. Use a subtle linear gradient transitioning from `primary` to `primary_container`.
*   **Nesting:** When placing a component inside a card, the inner component should use a surface tier one level higher (e.g., a `surface-variant` chip inside a `surface-container-high` card) to create a physical sense of layering.

---

## 3. Typography: Editorial Authority

We use **Plus Jakarta Sans** as our sole typeface. It is a modern, high-performance sans-serif that balances geometric precision with editorial warmth.

*   **Display & Headlines:** Use `display-lg` and `headline-lg` with tight letter spacing (-0.02em) to create an authoritative, "magazine" feel.
*   **Body Copy:** Prioritize `body-lg` for readability. Use `on_surface_variant` for secondary information to maintain a high-contrast hierarchy without cluttering the visual field.
*   **Intentional Scale:** The jump between `headline-sm` (1.5rem) and `label-sm` (0.6875rem) should be used aggressively to guide the eye toward the primary action while keeping metadata secondary.

---

## 4. Elevation & Depth: The Layering Principle

### Tonal Layering
Depth is achieved through the "stacking" of surface tokens. 
*   **Level 0 (Background):** `surface`
*   **Level 1 (Sections):** `surface-container-low`
*   **Level 2 (Cards):** `surface-container-high`
*   **Level 3 (Floating Elements):** Glassmorphic overlays with `surface-bright`.

### Ambient Shadows
Traditional drop shadows are forbidden. If a "floating" effect is required (e.g., a FAB or active Modal):
*   **Blur:** 30px - 60px.
*   **Opacity:** 4% - 10%.
*   **Color:** Use a tinted version of `primary` or `secondary` rather than black to simulate light passing through colored glass.

### The "Ghost Border" Fallback
Where accessibility requires a container edge, use the `outline-variant` token at **15% opacity**. This creates a "specular highlight" effect rather than a structural border.

---

## 5. Components

### Buttons & Interaction
*   **Primary Button:** Uses `primary_fixed` with a soft inner glow. Radius is always `full` (9999px) or `xl` (3rem).
*   **Secondary/Ghost:** `outline` token at low opacity with a high-blur backdrop.
*   **Progress Rings:** Utilize `secondary` (Teal) with a `particle bloom` effect—a soft outer glow that mimics a neon tube.

### Cards & Lists
*   **Forbid Dividers:** Do not use lines between list items. Use vertical white space (from the `md` or `lg` scale) to separate content.
*   **The Gloss Card:** Use `surface-container-high`, a 24px+ radius, and a subtle top-down gradient (10% opacity white to 0% opacity).

### Input Fields
*   **Stateful Design:** Inactive states use `surface-container-highest`. Active/Focus states trigger a `primary` "Ghost Border" and a subtle glow.

---

## 6. Do's and Don'ts

### Do:
*   **Do** use generous padding. If it feels like "too much" white space, it is likely correct for this premium aesthetic.
*   **Do** use circular avatars with `primary` or `tertiary` ring offsets to make users feel like "featured" entities.
*   **Do** apply "Soft Inner Glows" (inner shadows) to glassmorphic panels to simulate the thickness of a glass slab.

### Don't:
*   **Don't** use pure white (#FFFFFF) for text. Use `on_surface` (#f9f5f8) to prevent harsh "vibrating" against the dark background.
*   **Don't** use standard Android square corners. Every interactive element must follow the Roundedness Scale (`24px` minimum for cards).
*   **Don't** use flat, 2D icons for primary navigation. Use high-fidelity, 3D-styled illustrations or multi-tonal glyphs.

---

## 7. Spacing & Grid
The grid is a suggestion; the content is the driver. 
*   Use **intentional asymmetry**: Shift a headline 8dp to the left of the body copy to create a "broken grid" editorial look.
*   **Vertical Rhythm:** Always use multiples of 8dp, but favor large gaps (32dp, 48dp) to maintain the "luxurious" feel of the interface.
