$version = '4.8.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'AA8B0B29E1D06C28BD2FE2D9872CC41915433F1668BA5FA13467AE499A68CB57'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
