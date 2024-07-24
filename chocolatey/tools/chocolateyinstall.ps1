$version = '4.5.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '245CC5582493B317E65E157CEEBDB8E6D6CDDC2645E64627CC69B0B03ECE68B2'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
