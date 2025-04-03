export interface FilterSidebarProps {
  selectedBrand: string;
  selectedModel: string;
  models: string[];
  onBrandChange: (brand: string) => void;
  onModelChange: (model: string) => void; 
  priceFrom: string;
  priceTo: string;
  onPriceChange: (type: "from" | "to", value: string) => void;
  yearFrom: string;
  yearTo: string;
  onYearChange: (type: "from" | "to", value: string) => void; 
}
