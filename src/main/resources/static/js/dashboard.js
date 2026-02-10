document.addEventListener('DOMContentLoaded', function() {
    const ctx = document.getElementById('contactChart').getContext('2d');
    const isDarkMode = document.documentElement.classList.contains('dark');
    
    // Data - using your numbers from the cards
    const favorites = 12;
    const withLinkedIn = 18;
    const uniqueEmails = 40;
    const totalContacts = favorites + withLinkedIn + uniqueEmails; // Now equals 70
    
    // Color schemes
    const backgroundColors = [
        '#F59E0B', // yellow-500 (favorites)
        '#6366F1', // indigo-500 (linkedin)
        '#8B5CF6'  // purple-500 (emails)
    ];
    
    const borderColor = isDarkMode ? '#1F2937' : '#FFFFFF';
    const textColor = isDarkMode ? '#F3F4F6' : '#111827';
    
    const contactChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Favorites', 'With LinkedIn', 'Unique Emails'], // Removed "Others"
            datasets: [{
                data: [favorites, withLinkedIn, uniqueEmails], // Only these three categories
                backgroundColor: backgroundColors,
                borderColor: borderColor,
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '65%',
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        color: textColor,
                        font: {
                            size: 14
                        }
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const label = context.label || '';
                            const value = context.raw || 0;
                            const percentage = Math.round((value / totalContacts) * 100);
                            return `${label}: ${value} (${percentage}%)`;
                        }
                    }
                },
                doughnutCenterText: {
                    text: totalContacts.toString(), // Now shows 70 (12+18+40)
                    color: isDarkMode ? '#60A5FA' : '#3B82F6',
                    fontStyle: 'bold',
                    minFontSize: 20,
                    maxFontSize: 40,
                    subtitle: 'Total',
                    lineHeight: 1.5
                }
            }
        },
        plugins: [{
            id: 'doughnutCenterText',
            beforeDraw: function(chart) {
                const {ctx, chartArea: {width, height}} = chart;
                const options = chart.options.plugins.doughnutCenterText;
                
                if (!options) return;
                
                ctx.save();
                
                // Dynamic font size calculation
                const text = options.text;
                const minSize = options.minFontSize || 20;
                const maxSize = options.maxFontSize || 40;
                
                let fontSize = maxSize;
                ctx.font = `bold ${fontSize}px Arial`;
                let textWidth = ctx.measureText(text).width;
                
                while (textWidth > width * 0.5 && fontSize > minSize) {
                    fontSize -= 1;
                    ctx.font = `bold ${fontSize}px Arial`;
                    textWidth = ctx.measureText(text).width;
                }
                
                // Set text style
                ctx.fillStyle = options.color || '#000';
                ctx.textAlign = 'center';
                ctx.textBaseline = 'middle';
                
                // Calculate position
                const x = width / 2;
                const y = height / 2;
                
                // Draw main text
                ctx.fillText(text, x, y);
                
                // Draw subtitle if exists
                if (options.subtitle) {
                    ctx.font = `bold ${fontSize * 0.6}px Arial`;
                    ctx.fillText(options.subtitle, x, y + (fontSize * options.lineHeight));
                }
                
                ctx.restore();
            }
        }]
    });

    // Watch for dark mode changes
    const observer = new MutationObserver(() => {
        contactChart.update();
    });
    observer.observe(document.documentElement, {
        attributes: true,
        attributeFilter: ['class']
    });
});