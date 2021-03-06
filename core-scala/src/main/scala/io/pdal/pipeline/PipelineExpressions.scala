/*
 * Copyright 2017 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.pdal.pipeline

import io.pdal.Pipeline

import io.circe.Json
import io.circe.generic.extras.ConfiguredJsonCodec

@ConfiguredJsonCodec
sealed trait PipelineExpr {
  def ~(other: PipelineExpr): PipelineConstructor = this :: other :: Nil

  def ~(other: Option[PipelineExpr]): PipelineConstructor =
    other.fold(this :: Nil)(o => this :: o :: Nil)

  def toPipeline: Pipeline = (this :: Nil).toPipeline
}

@ConfiguredJsonCodec
case class RawExpr(json: Json) extends PipelineExpr

@ConfiguredJsonCodec
case class Read(
  filename: String,
  spatialreference: Option[String] = None,
  tag: Option[String] = None,
  `type`: Option[ReaderType] = None // usually auto derived by pdal
) extends PipelineExpr

@ConfiguredJsonCodec
case class FauxRead(
  numPoints: Int,
  mode: String, // constant | random | ramp | uniform | normal
  stdevX: Option[Int] = None, // [default: 1]
  stdevY: Option[Int] = None, // [default: 1]
  stdevZ: Option[Int] = None, // [default: 1]
  meanX: Option[Int] = None, // [default: 0]
  meanY: Option[Int] = None, // [default: 0]
  meanZ: Option[Int] = None, // [default: 0]
  bounds: Option[String] = None, // [default: unit cube]
  spatialreference: Option[String] = None,
  tag: Option[String] = None,
  `type`: ReaderType = ReaderTypes.faux
) extends PipelineExpr

object GdalRead {
  def apply(filename: String, spatialreference: Option[String] = None, tag: Option[String] = None): Read =
    Read(filename, spatialreference, tag, Some(ReaderTypes.gdal))
}

@ConfiguredJsonCodec
case class GeoWaveRead(
  zookeeperUrl: String,
  instanceName: String,
  username: String,
  password: String,
  tableNamespace: String,
  featureTypeName: Option[String] = None, // [default: PDAL_Point]
  dataAdapter: Option[String] = None, // [default: FeatureCollectionDataAdapter]
  pointsPerEntry: Option[String] = None, // [default: 5000u]
  bounds: Option[String] = None, // [default: unit cube]
  spatialreference: Option[String] = None,
  tag: Option[String] = None,
  `type`: ReaderType = ReaderTypes.geowave
) extends PipelineExpr

@ConfiguredJsonCodec
case class GreyhoundRead(
  url: String,
  bounds: Option[String] = None, // [default: the entire resource]
  depthBegin: Option[Int] = None, // [default: 0]
  depthEnd: Option[Int] = None, // [default: 0]
  tilePath: Option[String] = None,
  filter: Option[Json] = None,
  threads: Option[Int] = None, // [default: 4]
  spatialreference: Option[String] = None,
  tag: Option[String] = None,
  `type`: ReaderType = ReaderTypes.greyhound
) extends PipelineExpr

@ConfiguredJsonCodec
case class Ilvis2Read(
  filename: String,
  mapping: Option[String] = None,
  metadata: Option[String] = None,
  spatialreference: Option[String] = None,
  tag: Option[String] = None,
  `type`: ReaderType = ReaderTypes.ilvis2
) extends PipelineExpr

@ConfiguredJsonCodec
case class MatlabRead(
  filename: String,
  struct: Option[String] = None, // [default: PDAL]
  `type`: ReaderType = ReaderTypes.matlab
) extends PipelineExpr

@ConfiguredJsonCodec
case class MbioRead(
  filename: String,
  format: String,
  `type`: ReaderType = ReaderTypes.mbio
) extends PipelineExpr

@ConfiguredJsonCodec
case class LasRead(
  filename: String,
  extraDims: Option[String] = None,
  compression: Option[String] = None,
  spatialreference: Option[String] = None,
  tag: Option[String] = None,
  useEbVlr: Option[String] = None,
  `type`: ReaderType = ReaderTypes.las
) extends PipelineExpr

object MrsidRead {
  def apply(filename: String, spatialreference: Option[String] = None, tag: Option[String] = None): Read =
    Read(filename, spatialreference, tag, Some(ReaderTypes.mrsid))
}

object NitfRead {
  def apply(filename: String, spatialreference: Option[String] = None, tag: Option[String] = None): Read =
    Read(filename, spatialreference, tag, Some(ReaderTypes.nitf))
}

@ConfiguredJsonCodec
case class NumpyRead(
  filename: String,
  dimension: Option[String] = None,
  x: Option[Int] = None,
  y: Option[Int] = None,
  z: Option[Int] = None,
  assignZ: Option[String] = None,
  `type`: ReaderType = ReaderTypes.numpy
)

@ConfiguredJsonCodec
case class OciRead(
  connection: String,
  query: String,
  xmlSchemaDump: Option[String] = None,
  populatePointsourceid: Option[String] = None,
  spatialreference: Option[String] = None,
  tag: Option[String] = None,
  `type`: ReaderType = ReaderTypes.oci
) extends PipelineExpr

object OptechRead {
  def apply(filename: String, spatialreference: Option[String] = None, tag: Option[String] = None): Read =
    Read(filename, spatialreference, tag, Some(ReaderTypes.optech))
}

object OsgRead {
  def apply(filename: String, spatialreference: Option[String] = None, tag: Option[String] = None): Read =
    Read(filename, spatialreference, tag, Some(ReaderTypes.osg))
}

object PcdRead {
  def apply(filename: String, spatialreference: Option[String] = None, tag: Option[String] = None): Read =
    Read(filename, spatialreference, tag, Some(ReaderTypes.pcd))
}

@ConfiguredJsonCodec
case class PgpointcloudRead(
  connection: String,
  table: String,
  schema: Option[String] = None, // [default: public]
  column: Option[String] = None, // [default: pa]
  spatialreference: Option[String] = None,
  tag: Option[String] = None,
  `type`: ReaderType = ReaderTypes.pgpointcloud
) extends PipelineExpr

object PlyRead {
  def apply(filename: String, spatialreference: Option[String] = None, tag: Option[String] = None): Read =
    Read(filename, spatialreference, tag, Some(ReaderTypes.ply))
}

object PtsRead {
  def apply(filename: String, spatialreference: Option[String] = None, tag: Option[String] = None): Read =
    Read(filename, spatialreference, tag, Some(ReaderTypes.pts))
}

@ConfiguredJsonCodec
case class QfitRead(
  filename: String,
  flipCoordinates: Option[Boolean] = None,
  scaleZ: Option[Double] = None,
  spatialreference: Option[String] = None,
  tag: Option[String] = None,
  `type`: ReaderType = ReaderTypes.qfit
) extends PipelineExpr

@ConfiguredJsonCodec
case class RxpRead(
  filename: String,
  rdtp: Option[Boolean] = None,
  syncToPps: Option[Boolean] = None,
  minimal: Option[Boolean] = None,
  reflectanceAsIntensity: Option[Boolean] = None,
  minReflectance: Option[Double] = None,
  maxReflectance: Option[Double] = None,
  spatialreference: Option[String] = None,
  tag: Option[String] = None,
  `type`: ReaderType = ReaderTypes.rxp
) extends PipelineExpr

object SbetRead {
  def apply(filename: String, spatialreference: Option[String] = None, tag: Option[String] = None): Read =
    Read(filename, spatialreference, tag, Some(ReaderTypes.sbet))
}

@ConfiguredJsonCodec
case class SqliteRead(
  connection: String,
  query: String,
  spatialreference: Option[String] = None,
  tag: Option[String] = None,
  `type`: ReaderType = ReaderTypes.sqlite
) extends PipelineExpr

@ConfiguredJsonCodec
case class TextRead(
  filename: String,
  separator: Option[String] = None,
  spatialreference: Option[String] = None,
  header: Option[String] = None,
  skip: Option[Int] = None,
  count: Option[Long] = None,
  `type`: ReaderType = ReaderTypes.text
) extends PipelineExpr

@ConfiguredJsonCodec
case class TindexRead(
  filename: String,
  layerName: Option[String] = None,
  srsColumn: Option[String] = None,
  tindexName: Option[String] = None,
  sql: Option[String] = None,
  wkt: Option[String] = None,
  boundary: Option[String] = None,
  tSrs: Option[String] = None,
  filterSrs: Option[String] = None,
  where: Option[String] = None,
  dialect: Option[String] = None,
  spatialreference: Option[String] = None,
  tag: Option[String] = None,
  `type`: ReaderType = ReaderTypes.tindex
) extends PipelineExpr

object TerrasolidRead {
  def apply(filename: String, spatialreference: Option[String] = None, tag: Option[String] = None): Read =
    Read(filename, spatialreference, tag, Some(ReaderTypes.terrasolid))
}

object IceBridgeRead {
  def apply(filename: String, spatialreference: Option[String] = None, tag: Option[String] = None): Read =
    Read(filename, spatialreference, tag, Some(ReaderTypes.icebridge))
}

@ConfiguredJsonCodec
case class ApproximateCoplanarFilter(
  knn: Option[Int] = None, // [default: 8]
  thresh1: Option[Int] = None, // [default: 25]
  thresh2: Option[Int] = None, // [default: 6]
  `type`: FilterType = FilterTypes.approximatecoplanar
) extends PipelineExpr

@ConfiguredJsonCodec
case class ChipperFilter(
  capacity: Option[Int] = None, // [default: 5000]
  `type`: FilterType = FilterTypes.chipper
) extends PipelineExpr

@ConfiguredJsonCodec
case class ClusterFilter(
  minPoints: Option[Int] = None, // [default: 1]
  maxPoints: Option[Int] = None, // [default: UINT64_MAX]
  tolerance: Option[Double] = None, // [default: 1.0]
  `type`: FilterType = FilterTypes.cluster
)

@ConfiguredJsonCodec
case class ColorinterpFilter(
  ramp: Option[String] = None, // [default: pestel_shades]
  dimension: Option[String] = None, // [default: Z]
  minimum: Option[String] = None,
  maximum: Option[String] = None,
  invert: Option[Boolean] = None, // [default: false]
  k: Option[Double] = None,
  mad: Option[Boolean] = None,
  madMultiplier: Option[Double] = None,
  `type`: FilterType = FilterTypes.colorinterp
) extends PipelineExpr

@ConfiguredJsonCodec
case class ColorizationFilter(
  raster: String,
  dimensions: Option[String] = None,
  `type`: FilterType = FilterTypes.colorization
) extends PipelineExpr

@ConfiguredJsonCodec
case class ComputerangeFilter(
  `type`: FilterType = FilterTypes.computerange
) extends PipelineExpr

@ConfiguredJsonCodec
case class CpdFilter(
  method: Option[String] = None,
  `type`: FilterType = FilterTypes.cpd
) extends PipelineExpr

@ConfiguredJsonCodec
case class CropFilter(
  bounds: Option[String] = None,
  polygon: Option[String] = None,
  outside: Option[String] = None,
  point: Option[String] = None,
  radius: Option[String] = None,
  `type`: FilterType = FilterTypes.crop
) extends PipelineExpr

@ConfiguredJsonCodec
case class DecimationFilter(
  step: Option[Int] = None,
  offset: Option[Int] = None,
  limit: Option[Int] = None,
  `type`: FilterType = FilterTypes.decimation
) extends PipelineExpr

@ConfiguredJsonCodec
case class DividerFilter(
   mode: Option[String] = None,
   count: Option[Int] = None,
   capacity: Option[Int] = None,
  `type`: FilterType = FilterTypes.divider
) extends PipelineExpr

@ConfiguredJsonCodec
case class EigenValuesFilter(
  knn: Option[Int] = None,
  `type`: FilterType = FilterTypes.eigenvalues
) extends PipelineExpr

@ConfiguredJsonCodec
case class EstimateRankFilter(
  knn: Option[Int] = None,
  thresh: Option[Double] = None,
  `type`: FilterType = FilterTypes.estimaterank
) extends PipelineExpr

@ConfiguredJsonCodec
case class FerryFilter(
  dimensions: String,
  `type`: FilterType = FilterTypes.ferry
) extends PipelineExpr

@ConfiguredJsonCodec
case class GreedyProjectionFilter(
  `type`: FilterType = FilterTypes.greedyprojection
) extends PipelineExpr

@ConfiguredJsonCodec
case class GridProjectionFilter(
  `type`: FilterType = FilterTypes.gridprojection
) extends PipelineExpr

@ConfiguredJsonCodec
case class GroupByFilter(
  dimension: String,
  `type`: FilterType = FilterTypes.groupby
)

@ConfiguredJsonCodec
case class HagFilter(
  `type`: FilterType = FilterTypes.hag
) extends PipelineExpr

@ConfiguredJsonCodec
case class HeadFilter(
  count: Option[Int] = None, // [default: 10]
  `type`: FilterType = FilterTypes.head
) extends PipelineExpr

@ConfiguredJsonCodec
case class HexbinFilter(
  edgeSize: Option[Int] = None,
  sampleSize: Option[Int] = None,
  threshold: Option[Int] = None,
  precision: Option[Int] = None,
  `type`: FilterType = FilterTypes.hexbin
) extends PipelineExpr

@ConfiguredJsonCodec
case class IcpFilter(
  `type`: FilterType = FilterTypes.icp
) extends PipelineExpr

@ConfiguredJsonCodec
case class IqrFilter(
  dimension: String,
  k: Option[Double] = None,
  `type`: FilterType = FilterTypes.iqr
) extends PipelineExpr

@ConfiguredJsonCodec
case class KDistanceFilter(
  k: Option[Int] = None,
  `type`: FilterType = FilterTypes.kdistance
) extends PipelineExpr

@ConfiguredJsonCodec
case class LocateFilter(
  dimension: String,
  minmax: String,
  `type`: FilterType = FilterTypes.locate
) extends PipelineExpr

@ConfiguredJsonCodec
case class LofFilter(
  minpts: Option[Int] = None,
  `type`: FilterType = FilterTypes.lof
) extends PipelineExpr

@ConfiguredJsonCodec
case class MadFilter(
  dimension: String,
  k: Option[Double] = None,
  `type`: FilterType = FilterTypes.mad
) extends PipelineExpr

@ConfiguredJsonCodec
case class MatlabFilter(
  script: String,
  source: String,
  addDimension: Option[String] = None,
  struct: Option[String] = None, // [default: PDAL]
  `type`: FilterType = FilterTypes.matlab
) extends PipelineExpr

@ConfiguredJsonCodec
case class MergeFilter(
  inputs: List[String],
  tag: Option[String] = None,
  `type`: FilterType = FilterTypes.merge
) extends PipelineExpr

@ConfiguredJsonCodec
case class MongusFilter(
  cell: Option[Double] = None,
  classify: Option[Boolean] = None,
  extract: Option[Boolean] = None,
  k: Option[Double] = None,
  l: Option[Int] = None,
  `type`: FilterType = FilterTypes.mongus
) extends PipelineExpr

@ConfiguredJsonCodec
case class MortonOrderFilter(
  reverse: Option[String] = None,
  `type`: FilterType = FilterTypes.mortonorder
) extends PipelineExpr

@ConfiguredJsonCodec
case class MovingLeastSquaresFilter(
  `type`: FilterType = FilterTypes.movingleastsquares
) extends PipelineExpr

@ConfiguredJsonCodec
case class NormalFilter(
  knn: Option[Int] = None,
  `type`: FilterType = FilterTypes.normal
) extends PipelineExpr

@ConfiguredJsonCodec
case class OutlierFilter(
  method: Option[String] = None,
  minK: Option[Int] = None,
  radius: Option[Double] = None,
  meanK: Option[Int] = None,
  multiplier: Option[Double] = None,
  `type`: FilterType = FilterTypes.outlier
) extends PipelineExpr

@ConfiguredJsonCodec
case class OverlayFilter(
  dimension: Option[String] = None, // [default: none]
  datasource: Option[String] = None, // [default: none]
  column: Option[String] = None, // [default: none]
  query: Option[String] = None, // [default: first column]
  layer: Option[String] = None, // [default: first layer]
  `type`: FilterType = FilterTypes.overlay
) extends PipelineExpr

@ConfiguredJsonCodec
case class PclBlockFilter(
  filename: String,
  methods: Option[List[String]] = None,
  `type`: FilterType = FilterTypes.pclblock
) extends PipelineExpr

@ConfiguredJsonCodec
case class PmfFilter(
  maxWindowSize: Option[Int] = None,
  slope: Option[Double] = None,
  maxDistance: Option[Double] = None,
  initialDistance: Option[Double] = None,
  cellSize: Option[Int] = None,
  exponential: Option[Boolean] = None, // [default: true]
  `type`: FilterType = FilterTypes.pmf
) extends PipelineExpr

@ConfiguredJsonCodec
case class PoissonFilter(
  depth: Option[Int] = None,
  pointWeight: Option[Double] = None,
  `type`: FilterType = FilterTypes.poisson
) extends PipelineExpr

@ConfiguredJsonCodec
case class PythonFilter(
  module: String,
  function: String,
  script: Option[String] = None,
  source: Option[String] = None,
  addDimension: Option[String] = None,
  pdalargs: Option[String] = None,
  `type`: FilterType = FilterTypes.python
) extends PipelineExpr

@ConfiguredJsonCodec
case class RadialDensityFilter(
  radius: Option[Double] = None,
  `type`: FilterType = FilterTypes.radialdensity
) extends PipelineExpr

@ConfiguredJsonCodec
case class RandomizeFilter(
  `type`: FilterType = FilterTypes.randomize
) extends PipelineExpr

@ConfiguredJsonCodec
case class RangeFilter(
  limits: Option[String] = None,
  `type`: FilterType = FilterTypes.range
) extends PipelineExpr

@ConfiguredJsonCodec
case class ReprojectionFilter(
  outSrs: String,
  inSrs: Option[String] = None,
  tag: Option[String] = None,
  `type`: FilterType = FilterTypes.reprojection
) extends PipelineExpr

@ConfiguredJsonCodec
case class SampleFilter(
  radius: Option[Double] = None,
  `type`: FilterType = FilterTypes.sample
) extends PipelineExpr

@ConfiguredJsonCodec
case class SmrfFilter(
  cell: Option[Double] = None,
  classify: Option[Boolean] = None,
  cut: Option[Double] = None,
  extract: Option[Boolean] = None,
  slope: Option[Double] = None,
  threshold: Option[Double] = None,
  window: Option[Double] = None,
  `type`: FilterType = FilterTypes.smrf
) extends PipelineExpr

@ConfiguredJsonCodec
case class SortFilter(
  dimension: String,
  `type`: FilterType = FilterTypes.sort
) extends PipelineExpr

@ConfiguredJsonCodec
case class SplitterFilter(
  length: Option[Int] = None,
  originX: Option[Double] = None,
  originY: Option[Double] = None,
  `type`: FilterType = FilterTypes.splitter
) extends PipelineExpr

@ConfiguredJsonCodec
case class StatsFilter(
  dimenstions: Option[String] = None,
  enumerate: Option[String] = None,
  count: Option[Int] = None,
  `type`: FilterType = FilterTypes.stats
) extends PipelineExpr

@ConfiguredJsonCodec
case class TailFilter(
  count: Option[Int] = None, // [default: 10]
  `type`: FilterType = FilterTypes.tail
) extends PipelineExpr

@ConfiguredJsonCodec
case class TransformationFilter(
  matrix: String,
  `type`: FilterType = FilterTypes.transformation
) extends PipelineExpr

@ConfiguredJsonCodec
case class VoxelCenterNearestNeighborFilter(
  cell: Option[Double] = None, // [default: 1.0]
  `type`: FilterType = FilterTypes.voxelcenternearestneighbor
) extends PipelineExpr

@ConfiguredJsonCodec
case class VoxelCentroidNearestNeighbor(
  cell: Option[Double] = None, // [default: 1.0]
  `type`: FilterType = FilterTypes.voxelcentroidnearestneighbor
) extends PipelineExpr

@ConfiguredJsonCodec
case class VoxelGridFilter(
  leafX: Option[Double] = None,
  leafY: Option[Double] = None,
  leafZ: Option[Double] = None,
  `type`: FilterType = FilterTypes.voxelgrid
) extends PipelineExpr

@ConfiguredJsonCodec
case class Write(
  filename: String,
  `type`: Option[WriterType] = None // usually auto derived by pdal
) extends PipelineExpr

@ConfiguredJsonCodec
case class BpfWrite(
  filename: String,
  compression: Option[Boolean] = None,
  format: Option[String] = None,
  bundledfile: Option[String] = None,
  headerData: Option[String] = None,
  coordId: Option[String] = None,
  scaleX: Option[Double] = None,
  scaleY: Option[Double] = None,
  scaleZ: Option[Double] = None,
  offsetX: Option[String] = None,
  offsetY: Option[String] = None,
  offsetZ: Option[String] = None,
  outputDims: Option[String] = None,
  `type`: WriterType = WriterTypes.bpf
) extends PipelineExpr

@ConfiguredJsonCodec
case class GdalWrite(
  filename: String,
  resolution: Int,
  radius: Double,
  gdaldriver: Option[String] = None,
  gdalopts: Option[String] = None,
  outputType: Option[String] = None,
  windowSize: Option[Int] = None,
  dimension: Option[String] = None,
  `type`: WriterType = WriterTypes.gdal
) extends PipelineExpr

@ConfiguredJsonCodec
case class GeoWaveWrite(
  zookeeperUrl: String,
  instanceName: String,
  username: String,
  password: String,
  tableNamespace: String,
  featureTypeName: Option[String] = None,
  dataAdapter: Option[String] = None,
  pointsPerEntry: Option[String] = None, // [default: 5000u]
  `type`: WriterType = WriterTypes.geowave
) extends PipelineExpr

@ConfiguredJsonCodec
case class LasWrite(
  filename: String,
  forward: Option[String] = None,
  minorVersion: Option[Int] = None,
  softwareId: Option[String] = None,
  creationDoy: Option[Int] = None,
  creationYear: Option[Int] = None,
  dataformatId: Option[Int] = None,
  systemId: Option[String] = None,
  aSrs: Option[String] = None,
  globalEncoding: Option[String] = None,
  projectId: Option[String] = None,
  compression: Option[String] = None,
  scaleX: Option[Double] = None,
  scaleY: Option[Double] = None,
  scaleZ: Option[Double] = None,
  offsetX: Option[String] = None,
  offsetY: Option[String] = None,
  offsetZ: Option[String] = None,
  filesourceId: Option[Int] = None,
  discardHighReturnNumbers: Option[Boolean] = None,
  `type`: WriterType = WriterTypes.las
) extends PipelineExpr

@ConfiguredJsonCodec
case class MatlabWrite(
  filename: String,
  outputDims: Option[String] = None,
  `type`: WriterType = WriterTypes.matlab
) extends PipelineExpr

@ConfiguredJsonCodec
case class NitfWrite(
  filename: String,
  clevel: Option[String] = None,
  stype: Option[String] = None,
  ostaid: Option[String] = None,
  ftitle: Option[String] = None,
  fscalas: Option[String] = None,
  oname: Option[String] = None,
  ophone: Option[String] = None,
  fsctlh: Option[String] = None,
  fsclsy: Option[String] = None,
  idatim: Option[String] = None,
  iid2: Option[String] = None,
  fscltx: Option[String] = None,
  aimidb: Option[String] = None,
  acftb: Option[String] = None,
  `type`: WriterType = WriterTypes.nitf
) extends PipelineExpr

@ConfiguredJsonCodec
case class NullWrite(
  `type`: WriterType = WriterTypes.`null`
) extends PipelineExpr

@ConfiguredJsonCodec
case class OciWrite(
  connection: String,
  is3d: Option[Boolean] = None,
  solid: Option[Boolean] = None,
  overwrite: Option[Boolean] = None,
  verbose: Option[Boolean] = None,
  srid: Option[Int] = None,
  capacity: Option[Int] = None,
  streamOutputPrecision: Option[Int] = None,
  cloudId: Option[Int] = None,
  blockTableName: Option[String] = None,
  blockTablePartitionValue: Option[Int] = None,
  baseTableName: Option[String] = None,
  cloudColumnName: Option[String] = None,
  baseTableAuxColumns: Option[String] = None,
  baseTableAuxValues: Option[String] = None,
  baseTableBoundaryColumn: Option[String] = None,
  baseTableBoundaryWkt: Option[String] = None,
  preBlockSql: Option[String] = None,
  preSql: Option[String] = None,
  postBlockSql: Option[String] = None,
  baseTableBounds: Option[String] = None,
  pcId: Option[Int] = None,
  packIgnoredFields: Option[Boolean] = None,
  streamChunks: Option[Boolean] = None,
  blobChunkCount: Option[Int] = None,
  scaleX: Option[Double] = None,
  scaleY: Option[Double] = None,
  scaleZ: Option[Double] = None,
  offsetX: Option[Double] = None,
  offsetY: Option[Double] = None,
  offsetZ: Option[Double] = None,
  outputDims: Option[String] = None,
  `type`: WriterType = WriterTypes.oci
) extends PipelineExpr

@ConfiguredJsonCodec
case class PcdWrite(
  filename: String,
  compression: Option[Boolean] = None,
  `type`: WriterType = WriterTypes.pcd
) extends PipelineExpr

@ConfiguredJsonCodec
case class PgpointcloudWrite(
  connection: String,
  table: String,
  schema: Option[String] = None,
  column: Option[String] = None,
  compression: Option[String] = None,
  overwrite: Option[Boolean] = None,
  srid: Option[Int] = None,
  pcid: Option[Int] = None,
  preSql: Option[String] = None,
  postSql: Option[String] = None,
  scaleX: Option[Double] = None,
  scaleY: Option[Double] = None,
  scaleZ: Option[Double] = None,
  offsetX: Option[Double] = None,
  offsetY: Option[Double] = None,
  offsetZ: Option[Double] = None,
  outputDims: Option[String] = None,
  `type`: WriterType = WriterTypes.pgpointcloud
) extends PipelineExpr

@ConfiguredJsonCodec
case class PlyWrite(
  filename: String,
  storageMode: Option[String] = None,
  `type`: WriterType = WriterTypes.ply
) extends PipelineExpr

@ConfiguredJsonCodec
case class RialtoWrite(
  filename: String,
  maxLevels: Option[Int] = None,
  overwrite: Option[Boolean] = None,
  `type`: WriterType = WriterTypes.rialto
) extends PipelineExpr

@ConfiguredJsonCodec
case class SqliteWrite(
  filename: String,
  cloudTableName: String,
  blockTableName: String,
  cloudColumnName: Option[String] = None,
  compression: Option[String] = None,
  overwrite: Option[Boolean] = None,
  preSql: Option[String] = None,
  postSql: Option[String] = None,
  scaleX: Option[Double] = None,
  scaleY: Option[Double] = None,
  scaleZ: Option[Double] = None,
  offsetX: Option[Double] = None,
  offsetY: Option[Double] = None,
  offsetZ: Option[Double] = None,
  outputDims: Option[String] = None,
  `type`: WriterType = WriterTypes.sqlite
) extends PipelineExpr

@ConfiguredJsonCodec
case class TextWrite(
  filename: String,
  format: Option[String] = None,
  order: Option[String] = None,
  precision: Option[Int] = None,
  keepUnspecified: Option[Boolean] = None,
  jscallback: Option[String] = None,
  quoteHeader: Option[String] = None,
  newline: Option[String] = None,
  delimiter: Option[String] = None,
  `type`: WriterType = WriterTypes.text
) extends PipelineExpr
