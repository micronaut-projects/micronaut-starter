$version = '2.4.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '14639F873267763F26A5A4913E783917DEFE1FCE6ACA04F15A4EBF97A5B3AFF3'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
