# stuy-cs-graphics_w05-curves

Due: Wednesday 03/10/20 8:00am  
GitHub link: https://github.com/mks66/curves.git  


It’s time to add curves to the graphics engine. Everything will work as before, we will have three more shapes to draw.

- Add the following commands to the parser
    - circle: adds a circle to the edge matrix - takes 4 parameters: cx, cy, cz, r
    - hermite: adds a hermite curve to the edge matrix - takes 8 parameters: x0, y0, x1, y1, rx0, ry0, rx1, ry1
        - The curve is between points (x0, y0) and (x1, y1).
        - rx0, ry0 and rx1, ry1 are the rates of change at each endpoint
    - bezier: adds a bezier curve to the edge matrix - takes 8 parameters: x0, y0, x1, y1, x2, y2, x3, y3
        - This curve is drawn between (x0, y0) and (x3, y3).
        - (x1, y1) and (x2, y2) are the influence points for the curve.
- For help creating your own image, you might want to use the following graphs:
    - Bezier: https://www.desmos.com/calculator/jydkmhkxac
    - Hermite: https://www.desmos.com/calculator/o9owo98ca5
- 
